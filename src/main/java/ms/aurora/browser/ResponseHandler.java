package ms.aurora.browser;

import java.io.InputStream;

/**
 * Callback for handling responses to page requests.
 * @author Rick
 */
public interface ResponseHandler {

    void handleResponse(InputStream inputStream);

}
