package ms.aurora.browser;

import ms.aurora.browser.exception.BrowsingException;

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
                    URLConnection connection = targetURL.openConnection();
                    handler.handleResponse(connection.getInputStream());
                } catch (Exception e) {
                    throw new BrowsingException(
                            "Problem occured while requesting "
                                    + context.getURLAsString(), e);
                }
            }
        };
    }
}
