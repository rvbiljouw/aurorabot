package ms.aurora.api.script;

import ms.aurora.api.Context;
import ms.aurora.api.script.task.EventBus;
import ms.aurora.api.script.task.TaskQueue;
import ms.aurora.api.script.task.impl.Randoms;
import ms.aurora.event.listeners.PaintListener;
import org.apache.log4j.Logger;

/**
 * @author Rick
 */
public abstract class Script extends Context implements Runnable {
    private final Logger logger = Logger.getLogger(getClass());
    private final TaskQueue taskQueue = new TaskQueue(this);
    private Thread taskQueueThread = new Thread(taskQueue);
    private ScriptState state = ScriptState.START;
    private EventBus eventBus = new EventBus();
    private Randoms randoms = new Randoms();

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

    public final synchronized ScriptState getState() {
        return this.state;
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
                int delay = 600;
                if (isLoggedIn() && getState() != ScriptState.PAUSED) {
                    int loopCycle = tick();
                    if (loopCycle == -1) {
                        break;
                    }
                    delay += loopCycle;
                }

                Thread.sleep(delay);
            }

            onFinish();
        } catch (Exception e) {
            error("Script error!", e);
            e.printStackTrace();
        }
        cleanup();
    }

    private void init() {
        if (!taskQueueThread.isAlive()) {
            if (this instanceof PaintListener) {
                getSession().getPaintManager().register((PaintListener) this);
            }
            taskQueue.add(randoms);
            taskQueueThread.start();
        }
    }

    private void cleanup() {
        if (this instanceof PaintListener) {
            getSession().getPaintManager().deregister((PaintListener) this);
        }
        taskQueue.remove(randoms);
        taskQueueThread.interrupt();
    }

    public TaskQueue getQueue() {
        return taskQueue;
    }

    public EventBus getEventBus() {
        return eventBus;
    }
}
