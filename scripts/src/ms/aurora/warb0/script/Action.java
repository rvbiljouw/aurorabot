package ms.aurora.warb0.script;

import ms.aurora.api.methods.Players;
import ms.aurora.api.wrappers.RSPlayer;

import java.util.logging.Logger;

import static ms.aurora.api.util.Utilities.sleepNoException;

/**
 * @author tobiewarburton
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

    public abstract void execute();

    public static RSPlayer me() {
        return Players.getLocal();
    }

    public static void sleep(int time) {
        sleepNoException(time);
    }
}
