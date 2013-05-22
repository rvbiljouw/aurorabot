package ms.aurora.core;

import org.apache.log4j.Logger;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static java.lang.Thread.currentThread;

/**
 * @author Rick
 */
public final class Repository {
    private static final Logger logger = Logger.getLogger(Repository.class);
    private static final Map<Integer, Session> sessionMap = new HashMap<Integer, Session>();
    private static final Map<ThreadGroup, Session> groupMap = new HashMap<ThreadGroup, Session>();

    private Repository() {
    }

    public static Session get(Integer appletHash) {
        return sessionMap.get(appletHash);
    }

    public static Session get(ThreadGroup threadGroup) {
        return groupMap.get(threadGroup);
    }

    public static Session set(Integer appletHash, Session session) {
        groupMap.put(currentThread().getThreadGroup(), session);
        return sessionMap.put(appletHash, session);
    }

    public static Collection<Session> getAll() {
        return sessionMap.values();
    }

    public static void foreach(SessionVisitor visitor) {
        for(Session session : getAll()) {
            visitor.visit(session);
        }
    }
}
