package ms.aurora.loader.exception;

/**
 * Exception thrown when the applet stub appears to be malformed.
 * Cases in which this may happen:
 *  - There is a parameter missing
 *  - ...
 * @author Rick
 */
public class MalformedStubException extends RuntimeException {

    public MalformedStubException(String message) {
        super(message);
    }

}
