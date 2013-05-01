package ms.aurora.api;

import ms.aurora.core.Session;
import ms.aurora.rt3.Client;
import org.jboss.logging.Logger;

import java.util.HashMap;
import java.util.Map;

import static java.lang.Thread.currentThread;
import static ms.aurora.api.methods.Widgets.getWidget;

/**
 * @author Rick
 */
public class Context {
    private static final Map<ThreadGroup, Context> contextMap = new HashMap<ThreadGroup, Context>();
    private static final Logger logger = Logger.getLogger(Context.class);
    private final Map<String, String> properties = new HashMap<String, String>();
    private ThreadGroup threadGroup;
    private Session session;

    public final Session getSession() {
        return session;
    }

    public final void setSession(Session session) {
        threadGroup = session.getThreadGroup();
        contextMap.put(session.getThreadGroup(), this);
        this.session = session;
        setDefaults();
    }

    private void setDefaults() {
        properties.put("interaction.walkTo", "true");
    }

    public static Client getClient() {
        if (get() != null && get().session.getApplet() != null) {
            return (Client) get().session.getApplet();
        }
        return null;
    }

    public static boolean isLoggedIn() {
        if (get() == null) {
            logger.error("Context not set, cannot check logged in state!");
        }
        return get() != null && get().isLoggedInInternal();
    }

    private boolean isLoggedInInternal() {
        if (getClient().getLoginIndex() != 30) {
            logger.debug("Login index: " + getClient().getLoginIndex());
        }

        if (getWidget(378, 6) != null) {
            logger.debug("Welcome screen is set.");
        }
        return getClient().getLoginIndex() == 30 && getWidget(378, 6) == null;
    }

    public static Context get() {
        ThreadGroup tg = currentThread().getThreadGroup();
        if (contextMap.containsKey(tg)) {
            return contextMap.get(tg);
        }
        return null;
    }

    public static void setProperty(String key, Object value) {
        Context instance = get();
        if(instance.properties.containsKey(key)) {
            instance.properties.remove(key);
        }
        instance.properties.put(key, value.toString());
    }

    public static String getProperty(String key) {
        Context instance = get();
        return instance.properties.get(key);
    }

    public ThreadGroup getThreadGroup() {
        return threadGroup;
    }
}
