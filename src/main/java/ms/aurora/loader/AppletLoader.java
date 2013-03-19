package ms.aurora.loader;

import ms.aurora.browser.Browser;
import ms.aurora.browser.Context;
import ms.aurora.browser.ContextBuilder;
import ms.aurora.loader.exception.AppletInitialisationException;
import ms.aurora.loader.web.ClientConfig;
import ms.aurora.loader.web.ClientDefinitionJSON;
import ms.aurora.transform.TransformingClassLoader;

import java.applet.Applet;
import java.net.JarURLConnection;
import java.net.URL;

public final class AppletLoader implements Runnable {
    private final Context browserContext;
    private final Browser browser;

    private CompletionListener listener;
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
        browser.setContext(ContextBuilder.get().domain("subjectdeleted.com").build());
        ClientDefinitionJSON definitionJSON = new ClientDefinitionJSON(browser);
        definitionJSON.visit();

        AppletStubImpl appletStub = new AppletStubImpl(clientConfig);

		/*
         * Temporary
		 */
        try {
            URL jarURL = new URL("jar:" + clientConfig.getDocumentBase()
                    + clientConfig.getArchiveName() + "!/");
            JarURLConnection jarConnection = (JarURLConnection) jarURL.openConnection();
            final ClassLoader classLoader = new TransformingClassLoader(
                    definitionJSON.getDefinition(), jarConnection.getJarFile());

            Class<?> appletClass = classLoader.loadClass(clientConfig
                    .getMainClass());
            Applet appletInstance = (Applet) appletClass.newInstance();
            appletInstance.setStub(appletStub);
            appletInstance.init();
            appletInstance.start();
            applet = appletInstance;
            if (listener != null) {
                listener.onCompletion(applet);
            }
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

    public void setCompletionListener(CompletionListener listener) {
        this.listener = listener;
    }

    public Applet getApplet() {
        return applet;
    }

    public static interface CompletionListener {

        public void onCompletion(Applet applet);

    }
}
