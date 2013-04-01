package ms.aurora.api;

import ms.aurora.core.Session;
import ms.aurora.input.InputManager;
import ms.aurora.rt3.Client;

import java.util.Map;

import static com.google.common.collect.Maps.newHashMap;
import static java.lang.Thread.currentThread;

/**
 * @author rvbiljouw
 */
public class Context {
    private static final Map<ThreadGroup, Context> contextMap = newHashMap();
    public final InputManager input;
    private ThreadGroup threadGroup;
    private Session session;

    public Context() {
        this.input = new InputManager(this);
    }

    public final Session getSession() {
        return session;
    }

    public final void setSession(Session session) {
        threadGroup = currentThread().getThreadGroup();
        contextMap.put(threadGroup, this);
        this.session = session;
    }

    public final Client getClient() {
        if(session == null) {
            System.out.println(currentThread().getThreadGroup().getName());
        }
        return (Client) session.getApplet();
    }

    public static Context get() {
        ThreadGroup tg = currentThread().getThreadGroup();
        if(contextMap.containsKey(tg)) {
            return contextMap.get(tg);
        }
        return null;
    }

    public ThreadGroup getThreadGroup() {
        return threadGroup;
    }
}
