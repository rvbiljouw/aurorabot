package ms.aurora.core.script.exception;

import ms.aurora.api.script.Script;

/**
 * @author rvbiljouw
 */
public class ScriptAlreadyRunningException extends RuntimeException {

    public ScriptAlreadyRunningException(Script runningScript) {
        super("A script was already running: " + runningScript.getManifest().name());
    }

}
