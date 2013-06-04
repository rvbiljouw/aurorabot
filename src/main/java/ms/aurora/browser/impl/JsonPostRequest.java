package ms.aurora.browser.impl;

import ms.aurora.browser.Request;

import java.util.HashMap;
import java.util.Map;

/**
 * @author rvbiljouw
 */
public class JsonPostRequest implements Request {
    private Map<String, String> requestParams = new HashMap<String, String>();
    private boolean overrideContext;
    private String url;
    private String page;
    private String json;

    public JsonPostRequest(String json) {
        this.json = json;
    }

    public JsonPostRequest(String page, String json) {
        this.json = json;
        this.page = page;
    }

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
            paramString += paramToken + paramName + "=" + paramValue;
            paramCounter++;
        }
        return paramString;
    }

    @Override
    public Request.Type getRequestType() {
        return Request.Type.POST_JSON;
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

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }
}
