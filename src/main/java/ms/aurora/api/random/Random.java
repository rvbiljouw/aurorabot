package ms.aurora.api.random;

import ms.aurora.api.Context;
import org.apache.log4j.Logger;


/**
 * @author tobiewarburton
 */
public abstract class Random extends Context {
    private final Logger logger = Logger.getLogger(Random.class);

    public abstract boolean activate();

    public abstract int loop();

    public void info(String message) {
        logger.info(message);
    }

    public void error(String message) {
        logger.error(message);
    }
}
