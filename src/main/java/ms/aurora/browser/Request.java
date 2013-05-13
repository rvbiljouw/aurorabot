package ms.aurora.browser;

/**
 * Abstract form of Request, which has to be implemented for
 * specific types of requests (GET, POST, PUT, multipart forms etc.)
 * @author Rick
 */
public interface Request {

    void setParam(String paramName, String paramValue);

    String getParams();

    void setPage(String page);

    String getUrl();

    void setUrl(String url);

    boolean isOverrideContext();

    void setOverrideContext(boolean overrideContext);

    Type getRequestType();

    public static enum Type {
        GET, POST
    }
}
