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

    public final void info(String message) {
        logger.info(message);
    }

    public final void debug(String message) {
        logger.debug(message);
    }

    public final void error(String message) {
        logger.error(message);
    }

    public final void error(String message, Throwable t) {
        logger.error(message, t);
    }

    public final void setState(PluginState state) {
        switch (state) {
            case INIT:
                init();
                startup();
                break;

            case ACTIVE:
                execute();
                break;

            case STOPPED:
                cleanup();
                shutdown();
                break;
        }
    }

    private void init() {
        if (this instanceof PaintListener) {
            getSession().getPaintManager().
                    register((PaintListener) this);
        }
    }

    private void shutdown() {
        if (this instanceof PaintListener) {
            getSession().getPaintManager().
                   deregister((PaintListener) this);
        }
    }



    public abstract void startup();

    public abstract void execute();

    public abstract void cleanup();

    public final boolean validate() {
        return getManifest() != null;
    }

    public final PluginManifest getManifest() {
        return getClass().getAnnotation(PluginManifest.class);
    }
}
