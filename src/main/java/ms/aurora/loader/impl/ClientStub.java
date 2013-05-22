package ms.aurora.loader.impl;

import ms.aurora.loader.ClientConfig;

import java.applet.AppletContext;
import java.applet.AppletStub;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * @author rvbiljouw
 */
public class ClientStub implements AppletStub {
    private final ClientContext context = new ClientContext();
    private final ClientConfig config;

    public ClientStub(ClientConfig config) {
        this.config = config;
    }

    @Override
    public boolean isActive() {
        return true;
    }

    @Override
    public URL getDocumentBase() {
        try {
            return new URL(config.getDocumentBase());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public URL getCodeBase() {
        try {
            return new URL(config.getDocumentBase());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String getParameter(String name) {
        return config.getParam(name);
    }

    @Override
    public AppletContext getAppletContext() {
        return context;
    }

    @Override
    public void appletResize(int width, int height) {

    }
}
