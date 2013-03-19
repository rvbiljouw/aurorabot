package ms.aurora.browser;

public final class ContextBuilder {
    public static final int HTTP_PORT = 80;
    public static final int HTTPS_PORT = 443;

    private String contextProtocol = "http";
    private String contextDomain = "localhost";
    private String contextToken = "";
    private int contextPort = HTTP_PORT;

    public ContextBuilder protocol(String contextProtocol) {
        this.contextProtocol = contextProtocol;
        return this;
    }

    public ContextBuilder domain(String contextDomain) {
        this.contextDomain = contextDomain;
        return this;
    }

    public ContextBuilder port(int contextPort) {
        this.contextPort = contextPort;
        return this;
    }

    public ContextBuilder token(String contextToken) {
        this.contextToken = contextToken;
        return this;
    }

    public Context build() {
        Context newContext = new Context(contextProtocol, contextDomain,
                contextToken, contextPort);
        return newContext;
    }

    public static ContextBuilder get() {
        return new ContextBuilder();
    }
}
