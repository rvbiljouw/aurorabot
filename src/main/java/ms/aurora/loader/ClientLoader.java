package ms.aurora.loader;

import ms.aurora.loader.impl.ClientStub;
import ms.aurora.sdn.net.api.Hooks;
import ms.aurora.transform.ClientClassLoader;
import org.apache.log4j.Logger;

import java.applet.Applet;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;

/**
 * @author rvbiljouw
 */
public class ClientLoader {
    private static final Logger logger = Logger.getLogger(ClientLoader.class);
    private final ClientConfig config;
    private Applet applet;

    public ClientLoader(ClientConfig config) {
        this.config = config;
    }

    public void start() {
        if (!isLoaded()) {
            try {
                logger.info("Starting new applet");
                ClientStub stub = new ClientStub(config);
                prepareApplet();
                applet.setStub(stub);
                applet.init();
                applet.start();
            } catch (ReflectiveOperationException e) {
                logger.error("Initialization failed", e);
            } catch (IOException e) {
                logger.error("I/O fault", e);
            }
        } else {
            logger.warn("Applet waswn't started, call stop()!");
        }
    }

    public void stop() {
        if (applet != null) {
            applet.stop();
            applet.destroy();
            applet = null;
        }
        System.gc();
    }

    public boolean isLoaded() {
        return applet != null && applet.isActive();
    }

    private JarURLConnection establishJarConnection() throws IOException {
        String docBase = config.getDocumentBase();
        String archive = config.getArchiveName();
        return (JarURLConnection)
                new URL("jar:" + docBase + archive + "!/")
                        .openConnection();
    }

    private void prepareApplet() throws ReflectiveOperationException, IOException {
        JarURLConnection connection = establishJarConnection();
        ClientClassLoader loader = new ClientClassLoader(
                Hooks.getHooks(), connection.getJarFile());
        Class<?> mainClass = loader.loadClass(config.getMainClass());
        applet = (Applet) mainClass.newInstance();
    }

    public Applet getApplet() {
        return applet;
    }

    public void setApplet(Applet applet) {
        this.applet = applet;
    }
}
