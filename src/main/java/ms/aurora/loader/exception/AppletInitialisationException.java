package ms.aurora.loader.exception;

/**
 * @author rvbiljouw
 */
public class AppletInitialisationException extends RuntimeException {

    public AppletInitialisationException(String message, Throwable nested) {
        super(message, nested);
    }

}
