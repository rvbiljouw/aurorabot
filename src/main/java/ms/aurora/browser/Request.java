package ms.aurora.browser;

public interface Request {

    public void setParam(String paramName, String paramValue);

    public String getParams();

    public void setPage(String page);

    public String getUrl();

    public void setUrl(String url);

    public boolean isOverrideContext();

    public void setOverrideContext(boolean overrideContext);

    public Type getRequestType();

    public static enum Type {
        GET, POST
    }
}
