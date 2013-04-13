package ms.aurora.loader;

import ms.aurora.loader.exception.MalformedStubException;
import ms.aurora.loader.web.ClientConfig;

import java.applet.AppletContext;
import java.applet.AppletStub;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

/**
 * Applet stub for the RuneScape applet
 * @author Rick
 */
public final class AppletStubImpl implements AppletStub {
    private final AppletContextImpl appletContextImpl = new AppletContextImpl(
            this);
    private Map<String, String> appletParams;
    private URL documentBase;

    public AppletStubImpl(ClientConfig config) {
        this.appletParams = config.getParameters();
        try {
            this.documentBase = new URL(config.getDocumentBase());
        } catch (MalformedURLException e) {
            throw new MalformedStubException("Failed to initialize applet stub.");
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
        if (!appletParams.containsKey(name)) {
            throw new MalformedStubException("Missing stub element " + name);
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
