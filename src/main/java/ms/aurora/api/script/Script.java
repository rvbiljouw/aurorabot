package ms.aurora.api.script;

import ms.aurora.api.ClientContext;
import org.apache.log4j.Logger;

/**
 * @author rvbiljouw
 */
public abstract class Script extends ClientContext implements Runnable {
    private final Logger logger = Logger.getLogger(getClass());
    private ScriptState state = ScriptState.START;

    public Script() {
        ClientContext.set(this);
    }

    public abstract int tick();

    public void info(String message) {
        logger.info(message);
    }

    public void debug(String message) {
        logger.debug(message);
    }

    public void error(String message) {
        logger.error(message);
    }

    public void error(String message, Throwable t) {
        logger.error(message, t);
    }

    public void setState(ScriptState state) {
        this.state = state;
    }

    @Override
    public void run() {
        ClientContext.set(this);
        input.initialize();
        while (!Thread.currentThread().isInterrupted()) {
            try {
                switch (state) {
                    case START:
                        onStart();
                        state = ScriptState.RUNNING;
                        break;


                    case RUNNING:
                        int loopResult = tick();
                        if (loopResult != -1) {
                            Thread.sleep(loopResult);
                        } else {
                            // Returning -1 means exit.
                            info("Exited by -1");
                            return;
                        }
                        break;

                    case PAUSED:
                        Thread.sleep(5000);
                        break;

                    case STOP:
                        return;

                }
            } catch (InterruptedException e) {
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

    public boolean validate() {
        return getManifest() != null;
    }

    public ScriptManifest getManifest() {
        return getClass().getAnnotation(ScriptManifest.class);
    }


}
