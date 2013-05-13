package ms.aurora.core.plugin.exception;

/**
 * Exception thrown when there is something wrong with a plugin.
 * @author Rick
 */
public class PluginException extends RuntimeException {

    public PluginException(String description) {
        super(description);
    }

    public PluginException(String description, Exception cause) {
        super(description, cause);
    }

}
