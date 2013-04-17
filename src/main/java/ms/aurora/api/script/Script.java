package ms.aurora.api.script;

import ms.aurora.api.Context;
import ms.aurora.event.listeners.PaintListener;
import org.apache.log4j.Logger;

import static java.lang.Thread.currentThread;

/**
 * @author Rick
 */
public abstract class Script extends Context implements Runnable {
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
    }

    public final synchronized ScriptState getState() {
        return this.state;
    }

    public void onStart() {

    }

    public void onFinish() {

    }

    private void init() {
        if (this instanceof PaintListener) {
            getSession().getPaintManager().register((PaintListener) this);
        }
    }

    private void cleanup() {
        if (this instanceof PaintListener) {
            getSession().getPaintManager().deregister((PaintListener) this);
        }
    }

    public final boolean validate() {
        return getManifest() != null;
    }

    public final ScriptManifest getManifest() {
        return getClass().getAnnotation(ScriptManifest.class);
    }

    @Override
    public final void run() {
        while (!currentThread().isInterrupted()) {
            try {
                switch (getState()) {
                    case START:
                        init();
                        onStart();
                        setState(ScriptState.RUNNING);
                        break;


                    case RUNNING:
                        if (Context.isLoggedIn()) {
                            int loopResult = tick();
                            if (loopResult != -1) {
                                Thread.sleep(loopResult + 600);
                            } else {
                                // Returning -1 means exit.
                                state = ScriptState.STOP;
                                info("Exited by -1");
                                cleanup();
                                onFinish();
                                return;
                            }
                        } else {
                            info("Not logged in.");
                            Thread.sleep(5000);
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
                destroy();
                return;
            } catch (Exception e) {
                // Any other exception we print the stack trace before destroying.
                logger.error("Script has thrown exception and has exited.", e);
                destroy();
                return;
            }
        }
    }

    public void destroy() {
        state = ScriptState.STOP;
        cleanup();
        onFinish();
        currentThread().interrupt();
    }
}
