package ms.aurora.api;

import ms.aurora.core.Session;
import ms.aurora.rt3.Client;

import java.util.Map;

import static com.google.common.collect.Maps.newHashMap;
import static java.lang.Thread.currentThread;
import static ms.aurora.api.methods.Widgets.getWidget;

/**
 * @author Rick
 */
public class Context {
    private static final Map<ThreadGroup, Context> contextMap = newHashMap();
    private ThreadGroup threadGroup;
    private Session session;

    public final Session getSession() {
        return session;
    }

    public final void setSession(Session session) {
        threadGroup = currentThread().getThreadGroup();
        contextMap.put(threadGroup, this);
        this.session = session;
    }

    public static Client getClient() {
        if (get() != null && get().session.getApplet() != null) {
            return (Client) get().session.getApplet();
        }
        return null;
    }

    public static boolean isLoggedIn() {
        return get() != null && get().isLoggedInInternal();
    }

    private boolean isLoggedInInternal() {
        return getClient().getLoginIndex() == 30 && getWidget(378, 6) == null;
    }

    public static Context get() {
        ThreadGroup tg = currentThread().getThreadGroup();
        if (contextMap.containsKey(tg)) {
            return contextMap.get(tg);
        }
        return null;
    }

    public ThreadGroup getThreadGroup() {
        return threadGroup;
    }
}
