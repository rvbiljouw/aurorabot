package ms.aurora.browser.exception;

import java.io.IOException;

/**
 * @author rvbiljouw
 */
public class ParsingException extends IOException {

    public ParsingException(String message, Exception nested) {
        super(message, nested);
    }

}
