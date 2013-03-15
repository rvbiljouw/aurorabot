package ms.aurora.browser;

public final class RequestBuilder {
    private Request cachedRequest;

    public RequestBuilder(Class<? extends Request> requestType) {
        type(requestType);
    }

    public RequestBuilder type(Class<? extends Request> requestType) {
        try {
            cachedRequest = requestType.newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Creation of a request with type "
                    + requestType.getSimpleName() + " failed.", e);
        }
        return this;
    }

    public RequestBuilder page(String page) {
        cachedRequest.setPage(page);
        return this;
    }

    public RequestBuilder with(String parameterName, String parameterValue) {
        cachedRequest.setParam(parameterName, parameterValue);
        return this;
    }

    public RequestBuilder url(String url) {
        cachedRequest.setUrl(url);
        cachedRequest.setOverrideContext(true);
        return this;
    }

    public Request create() {
        return cachedRequest;
    }

    public static RequestBuilder get(Class<? extends Request> requestType) {
        return new RequestBuilder(requestType);
    }

}
