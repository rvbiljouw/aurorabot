package ms.aurora.api.script;

import java.util.logging.Logger;

/**
 * @author tobiewarburton
 * @author A_C/Cov
 */
public abstract class Action {

    private final Logger logger = Logger.getLogger(getClass().getName());

    public final void info(String message) {
        logger.info(message);
    }

    public final void error(String message) {
        logger.severe(message);
    }

    public abstract boolean activate();

    public abstract int execute();

}
