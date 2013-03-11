package ms.aurora.loader;

import ms.aurora.loader.web.ClientConfig;

import java.applet.AppletContext;
import java.applet.AppletStub;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

public class AppletStubImpl implements AppletStub {
    private final AppletContextImpl appletContextImpl = new AppletContextImpl(
            this);
    private Map<String, String> appletParams;
    private URL documentBase;

    public AppletStubImpl(ClientConfig config) {
        this.appletParams = config.getParameters();
        try {
            this.documentBase = new URL(config.getDocumentBase());
        } catch (MalformedURLException e) {
            throw new RuntimeException("Failed to initialize applet stub.", e);
        }
    }

    @Override
    public boolean isActive() {
        return true;
    }

    @Override
    public URL getDocumentBase() {
        return documentBase;
    }

    @Override
    public URL getCodeBase() {
        return documentBase;
    }

    @Override
    public String getParameter(String name) {
        if (name.equals("6")) {
            return "http://www.runescape.com/slr.ws?order=LPWM";
        }
        return appletParams.get(name);
    }

    @Override
    public AppletContext getAppletContext() {
        return appletContextImpl;
    }

    @Override
    public void appletResize(int width, int height) {

    }

}
