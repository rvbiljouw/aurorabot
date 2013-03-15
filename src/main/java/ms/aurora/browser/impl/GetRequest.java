package ms.aurora.browser.impl;

import com.google.common.collect.Maps;
import ms.aurora.browser.Request;

import java.util.Map;

public class GetRequest implements Request {
    private Map<String, String> requestParams = Maps.newHashMap();
    private boolean overrideContext;
    private String url;
    private String page;

    @Override
    public void setPage(String page) {
        this.page = page;
    }

    @Override
    public void setParam(String paramName, String paramValue) {
        requestParams.put(paramName, paramValue);
    }

    @Override
    public String getParams() {
        String paramString = page;
        int paramCounter = 0;
        for (String paramName : requestParams.keySet()) {
            String paramValue = requestParams.get(paramName);
            String paramToken = paramCounter == 0 ? "?" : "&";
            paramString += new StringBuilder().append(paramToken).
                    append(paramName).append("=").
                    append(paramValue).toString();
        }
        return paramString;
    }

    @Override
    public Type getRequestType() {
        return Type.GET;
    }

    @Override
    public String getUrl() {
        return url;
    }

    @Override
    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public boolean isOverrideContext() {
        return overrideContext;
    }

    @Override
    public void setOverrideContext(boolean overrideContext) {
        this.overrideContext = overrideContext;
    }

}
