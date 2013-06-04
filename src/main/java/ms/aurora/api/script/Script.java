package ms.aurora.api.script;

import ms.aurora.api.Context;
import ms.aurora.api.script.task.EventBus;
import ms.aurora.api.script.task.TaskQueue;
import ms.aurora.api.script.task.impl.Randoms;
import ms.aurora.api.script.task.impl.Regions;
import ms.aurora.event.listeners.PaintListener;
import org.apache.log4j.Logger;

/**
 * @author Rick
 */
public abstract class Script extends Context implements Runnable {
    private final Logger logger = Logger.getLogger(getClass());
    private TaskQueue taskQueue;
    private Thread taskQueueThread;
    private ScriptState state = ScriptState.START;
    private Randoms randoms;

    public Script() {
    }

    public abstract int tick();

    public final void info(String message) {
        logger.info(message);
    }

    public final void debug(String message) {
        logger.debug(message);
    }

    public final void error(String message) {
        logger.error(message);
    }

    public final void error(String message, Throwable t) {
        logger.error(message, t);
    }

    public final synchronized ScriptState getState() {
        return this.state;
    }

    public final synchronized void setState(ScriptState state) {
        switch (state) {
            case PAUSED:
                onPause();
                break;
            case RUNNING:
                if (this.state == ScriptState.PAUSED) {
                    onResume();
                }
                break;
            default:
                break;
        }
        this.state = state;
    }

    public void onPause() {

    }

    public void onResume() {

    }

    public void onStart() {

    }

    public void onFinish() {

    }

    public final boolean validate() {
        return getManifest() != null;
    }

    public final ScriptManifest getManifest() {
        return getClass().getAnnotation(ScriptManifest.class);
    }

    @Override
    public final void run() {
        logger.info("Started " + getManifest().name() + " by " + getManifest().author());
        init();
        try {

            onStart();

            while (getState() != ScriptState.STOP) {
                String _delay = getProperty("script.delay");
                int delay = _delay == null ? 500 : Integer.parseInt(_delay);
                if (isLoggedIn() && getState() != ScriptState.PAUSED) {
                    int loopCycle = tick();
                    if (loopCycle < 0) {
                        break;
                    }
                    delay += loopCycle;
                }

                try {
                    Thread.sleep(delay);
                } catch (RuntimeException e) {
                    break;
                }
            }

            onFinish();
        } catch (Exception e) {
            error("Script error!", e);
            e.printStackTrace();
        }
        cleanup();
    }

    private void init() {
        if (taskQueueThread == null || !taskQueueThread.isAlive()) {
            Context.getEventBus().register(this);
            taskQueue = new TaskQueue(this);
            taskQueueThread = new Thread(getSession().getThreadGroup(), taskQueue);
            if (this instanceof PaintListener) {
                getSession().getPaintManager().register((PaintListener) this);
            }
            randoms = new Randoms();
            taskQueue.add(randoms);
            taskQueue.add(new Regions());
            taskQueueThread.start();
        }
    }

    private void cleanup() {
        if (this instanceof PaintListener) {
            getSession().getPaintManager().deregister((PaintListener) this);
            getSession().getScriptManager().stop();
        }
        taskQueue.remove(randoms);
        taskQueue.destruct();
        taskQueueThread.interrupt();
        Context.getEventBus().deregister(this);
    }

    public TaskQueue getQueue() {
        return taskQueue;
    }
}
