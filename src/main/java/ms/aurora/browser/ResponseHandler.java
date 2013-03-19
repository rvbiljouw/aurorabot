package ms.aurora.browser;

import java.io.InputStream;

public interface ResponseHandler {

    void handleResponse(InputStream inputStream);

}
