package ms.aurora.core.script.exception;

/**
 * @author rvbiljouw
 */
public class ScriptManifestException extends RuntimeException {

    public ScriptManifestException(String scriptName) {
        super("A script without a manifest was detected: " + scriptName);
    }

}
