package ms.aurora.loader;

import ms.aurora.api.methods.web.model.World;
import ms.aurora.browser.Browser;
import ms.aurora.browser.Context;
import ms.aurora.core.Session;
import ms.aurora.rt3.Client;
import org.apache.log4j.Logger;

import java.applet.Applet;

import static ms.aurora.browser.ContextBuilder.get;
import static ms.aurora.gui.Messages.getString;

/**
 * @author rvbiljouw
 */
public class ClientWrapper {
    private static final Logger logger = Logger.getLogger(ClientWrapper.class);
    private final String browserBaseURL = getString("runescape.url");
    private final Context browserContext = get().domain(browserBaseURL).build();
    private final Browser browser = new Browser(browserContext);
    private final ClientConfig config = new ClientConfig(this, browser);
    private final ClientLoader loader = new ClientLoader(config);
    private World world;

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

    public World getWorld() {
        return world;
    }

    public boolean setWorld(Session session, World world) {
        this.world = world;

        if (loader.isLoaded()) {
            session.getScriptManager().pause();
            restart();
            session.update();
            session.getScriptManager().resume();
            return true;
        }
        return false;
    }
}
