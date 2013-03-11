package ms.aurora.browser;

import java.net.MalformedURLException;
import java.net.URL;

public class Context {
    private final String protocol;
    private final String domain;
    private final String token;
    private final int port;

    public Context(String protocol, String domain, String token, int port) {
        this.protocol = protocol;
        this.domain = domain;
        this.token = token;
        this.port = port;
    }

    public String getURLAsString() {
        return protocol + "://" + domain + ":" + port + token;
    }

    public URL getURL() throws MalformedURLException {
        return new URL(getURLAsString());
    }

    public URL getParameterizedURL(String parameterString)
            throws MalformedURLException {
        return new URL(getURLAsString() + parameterString);
    }
}
