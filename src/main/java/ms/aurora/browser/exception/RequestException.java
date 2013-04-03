package ms.aurora.browser.exception;

/**
 * @author Rick
 */
public class RequestException extends RuntimeException {

    public RequestException(String message, Exception nested) {
        super(message, nested);
    }

}
