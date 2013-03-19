package ms.aurora.browser;

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
