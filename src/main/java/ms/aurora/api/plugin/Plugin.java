package ms.aurora.api.plugin;

import ms.aurora.api.ClientContext;
import ms.aurora.core.Session;
import ms.aurora.event.listeners.PaintListener;
import org.apache.log4j.Logger;

/**
 * @author rvbiljouw
 */
public abstract class Plugin extends ClientContext {
    private final Logger logger = Logger.getLogger(getClass());

    public Plugin() {
        ClientContext.set(this);
    }

    public void info(String message) {
        logger.info(message);
    }

    public void debug(String message) {
        logger.debug(message);
    }

    public void error(String message) {
        logger.error(message);
    }

    public void error(String message, Throwable t) {
        logger.error(message, t);
    }

    @Override
    public void setSession(Session session) {
        super.setSession(session);

        if (this instanceof PaintListener) {
            session.getPaintManager().register((PaintListener) this);
        }
    }

    public void setState(PluginState state) {
        switch (state) {
            case INIT:
                startup();
                break;

            case ACTIVE:
                execute();
                break;

            case STOPPED:
                cleanup();
                break;
        }
    }

    public abstract void startup();

    public abstract void execute();

    public abstract void cleanup();

    public boolean validate() {
        return getManifest() != null;
    }

    public PluginManifest getManifest() {
        return getClass().getAnnotation(PluginManifest.class);
    }
}
