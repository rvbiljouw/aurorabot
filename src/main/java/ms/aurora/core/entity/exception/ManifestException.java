package ms.aurora.core.entity.exception;

/**
 * Exception thrown when there is a problem with the manifest of a specified class.
 *
 * @author Rick
 */
public class ManifestException extends RuntimeException {
    public ManifestException(String description) {
        super(description);
    }
}