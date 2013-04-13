package ms.aurora.loader.exception;

/**
 * Exception thrown when the applet fails to initialize, for whatever reason.
 * @author Rick
 */
public class AppletInitialisationException extends RuntimeException {

    public AppletInitialisationException(String message, Throwable nested) {
        super(message, nested);
    }

}
