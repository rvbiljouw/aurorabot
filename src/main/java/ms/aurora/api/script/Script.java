package ms.aurora.api.script;

import ms.aurora.api.ClientContext;
import org.apache.log4j.Logger;

import static java.lang.Thread.currentThread;
import static ms.aurora.api.util.Utilities.sleepNoException;

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
        while(!currentThread().isInterrupted()) {
            switch(state) {

                case START:
                    onStart();
                    state = ScriptState.RUNNING;
                    break;

                case RUNNING:
                    int tick = tick();
                    sleepNoException(tick, tick + 100);
                    break;

                case STOP:
                    onFinish();
                    currentThread().interrupt();
                    break;

                case PAUSED:
                    sleepNoException(5000);
                    break;
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
