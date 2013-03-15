package ms.aurora.browser.exception;

/**
 * @author rvbiljouw
 */
public class BrowsingException extends RuntimeException {

    public BrowsingException(String message, Throwable nested) {
        super(message, nested);
    }

}
