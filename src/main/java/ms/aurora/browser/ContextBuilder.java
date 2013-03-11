package ms.aurora.browser;

public class ContextBuilder {
    private String contextProtocol = "http";
    private String contextDomain = "localhost";
    private String contextToken = "";
    private int contextPort = 80;

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
