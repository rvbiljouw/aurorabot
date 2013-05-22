package ms.aurora.loader;

import ms.aurora.browser.Browser;
import ms.aurora.browser.Context;
import ms.aurora.rt3.Client;
import org.apache.log4j.Logger;

import java.applet.Applet;

import static ms.aurora.gui.Messages.getString;
import static ms.aurora.browser.ContextBuilder.get;

/**
 * @author rvbiljouw
 */
public class ClientWrapper {
    private static final Logger logger = Logger.getLogger(ClientWrapper.class);
    private final String browserBaseURL = getString("runescape.url");
    private final Context browserContext = get().domain(browserBaseURL).build();
    private final Browser browser = new Browser(browserContext);
    private final ClientConfig config = new ClientConfig(browser);
    private final ClientLoader loader = new ClientLoader(config);

    public Client getClient() {
        return (Client) loader.getApplet();
    }

    public Applet getApplet() {
        return loader.getApplet();
    }

    public boolean start() {
        config.visit();
        loader.start();
        return loader.isLoaded();
    }

    public boolean stop() {
        loader.stop();
        return !loader.isLoaded();
    }

    public void restart() {
        if (stop() && start()) {
            logger.info("Applet restarted");
        } else {
            logger.error("Applet reload failed.");
        }
    }
}
