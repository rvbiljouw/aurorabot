package ms.aurora.core.script.exception;

/**
 * @author rvbiljouw
 */
public class NoScriptRunningException extends RuntimeException {

    public NoScriptRunningException() {
        super("No script was running.");
    }

}
