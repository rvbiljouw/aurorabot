package ms.aurora.browser;

import ms.aurora.browser.exception.BrowsingException;
import ms.aurora.browser.impl.JsonPostRequest;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

/**
 * A browser simulation, that supports synchronous and asynchronous I/O.
 * @author Rick
 */
public final class Browser {
    private Context context;

    public Browser() {

    }

    public Browser(Context context) {
        this.context = context;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public void doRequest(Request request, ResponseHandler handler) {
        request(request, handler, false);
    }

    public void doRequestAsync(Request request, ResponseHandler handler) {
        request(request, handler, true);
    }

    private void request(Request request, ResponseHandler handler, boolean async) {
        Runnable requestHandler = getRunnable(request, handler);
        Thread requestHandlerThread = new Thread(requestHandler);
        if (async) {
            requestHandlerThread.start();
        } else {
            requestHandlerThread.run();
        }
    }

    private Runnable getRunnable(final Request request,
                                 final ResponseHandler handler) {
        return new Runnable() {
            public void run() {
                try {
                    URL targetURL = request.isOverrideContext() ? new URL(
                            request.getUrl()) : context
                            .getParameterizedURL(request.getParams());
                    HttpURLConnection connection = (HttpURLConnection)targetURL.openConnection();
                    if(request.getRequestType() == Request.Type.POST_JSON) {
                        JsonPostRequest req = (JsonPostRequest)request;
                        connection.setRequestMethod("POST");
                        connection.setRequestProperty("Content-Type", "text/plain");
                        connection.setRequestProperty("Content-Length", String.valueOf(req.getJson().getBytes().length));
                        connection.setUseCaches(false);
                        connection.setDoOutput(true);
                        connection.setDoInput(true);
                        OutputStream os = connection.getOutputStream();
                        os.write(req.getJson().getBytes());
                        os.flush();
                    }
                    handler.handleResponse(connection.getInputStream());
                    connection.disconnect();
                } catch (Exception e) {
                    throw new BrowsingException(
                            "Problem occured while requesting "
                                    + context.getURLAsString(), e);
                }
            }
        };
    }
}
