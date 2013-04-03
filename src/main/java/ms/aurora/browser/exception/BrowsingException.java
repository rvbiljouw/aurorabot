package ms.aurora.browser.exception;

/**
 * @author Rick
 */
public class BrowsingException extends RuntimeException {
	private static final long serialVersionUID = 6606777982233074671L;

	public BrowsingException(String message, Throwable nested) {
        super(message, nested);
    }

}
