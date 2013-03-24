package ms.aurora.api.script;

import ms.aurora.api.ClientContext;
import ms.aurora.event.listeners.PaintListener;
import org.apache.log4j.Logger;

/**
 * @author rvbiljouw
 */
public abstract class Script extends ClientContext implements Runnable {
    private final Logger logger = Logger.getLogger(getClass());
    private ScriptState state = ScriptState.START;

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
        this.state = state;
        info("New state " + state.name());
    }

    public final synchronized ScriptState getState() {
        return this.state;
    }

    @Override
    public final void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                switch (getState()) {
                    case START:
                        init();
                        onStart();
                        setState(ScriptState.RUNNING);
                        break;


                    case RUNNING:
                        int loopResult = tick();
                        if (loopResult != -1) {
                            Thread.sleep(loopResult + 600);
                        } else {
                            // Returning -1 means exit.
                            info("Exited by -1");
                            return;
                        }
                        break;

                    case PAUSED:
                        Thread.sleep(1000);
                        break;

                    case STOP:
                        cleanup();
                        onFinish();
                        return;

                }
            } catch (InterruptedException e) {
                e.printStackTrace();
                Thread.currentThread().interrupt();
            } catch (Exception e) {
                logger.error("Script has thrown exception and has exited.", e);
                return;
            }
        }
    }

    public void onStart() {

    }

    public void onFinish() {

    }

    private void init() {
        input.initialize();

        if(this instanceof PaintListener) {
            getSession().getPaintManager().register((PaintListener)this);
        }
    }

    private void cleanup() {
        if(this instanceof PaintListener) {
            getSession().getPaintManager().deregister((PaintListener)this);
        }
    }

    public final boolean validate() {
        return getManifest() != null;
    }

    public final ScriptManifest getManifest() {
        return getClass().getAnnotation(ScriptManifest.class);
    }


}
