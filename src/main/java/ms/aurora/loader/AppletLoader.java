package ms.aurora.loader;

import ms.aurora.browser.Browser;
import ms.aurora.browser.Context;
import ms.aurora.browser.ContextBuilder;
import ms.aurora.loader.exception.AppletInitialisationException;
import ms.aurora.loader.web.ClientConfig;
import ms.aurora.sdn.net.api.Hooks;
import ms.aurora.transform.TransformingClassLoader;

import java.applet.Applet;
import java.net.JarURLConnection;
import java.net.URL;

public final class AppletLoader implements Runnable {
    private final Context browserContext;
    private final Browser browser;
    private Applet applet;
    private Thread self;

    public AppletLoader() {
        browserContext = ContextBuilder.get().domain("oldschool.runescape.com")
                .build();
        browser = new Browser(browserContext);
        self = new Thread(this);
    }

    public AppletLoader(Language aLanguage) {
        browserContext = ContextBuilder.get().domain("oldschool.runescape.com")
                .token(aLanguage.toString()).build();
        browser = new Browser(browserContext);
        self = new Thread(this);
    }

    @Override
    public void run() {
        ClientConfig clientConfig = new ClientConfig(browser);
        clientConfig.visit();

        AppletStubImpl appletStub = new AppletStubImpl(clientConfig);
        try {
            URL jarURL = new URL("jar:" + clientConfig.getDocumentBase()
                    + clientConfig.getArchiveName() + "!/");
            JarURLConnection jarConnection = (JarURLConnection) jarURL.openConnection();
            final ClassLoader classLoader = new TransformingClassLoader(
                    Hooks.getHooks(), jarConnection.getJarFile());

            Class<?> appletClass = classLoader.loadClass(clientConfig
                    .getMainClass());
            Applet appletInstance = (Applet) appletClass.newInstance();
            appletInstance.setStub(appletStub);
            appletInstance.init();
            appletInstance.start();
            applet = appletInstance;
        } catch (Exception e) {
            throw new AppletInitialisationException("Failed to load applet!", e);
        }
    }

    public void start() {
        self.start();
    }

    public void interrupt() {
        self.interrupt();
    }

    public Applet getApplet() {
        return applet;
    }
}
