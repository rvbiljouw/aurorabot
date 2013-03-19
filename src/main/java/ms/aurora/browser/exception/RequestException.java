package ms.aurora.browser.exception;

/**
 * @author rvbiljouw
 */
public class RequestException extends RuntimeException {

    public RequestException(String message, Exception nested) {
        super(message, nested);
    }

}
