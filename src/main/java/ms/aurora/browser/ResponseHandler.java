package ms.aurora.browser;

import java.io.InputStream;

public interface ResponseHandler {

    public void handleResponse(InputStream inputStream);

}
