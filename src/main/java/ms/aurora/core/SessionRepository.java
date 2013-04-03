package ms.aurora.core;

import java.util.Collection;
import java.util.Map;

import static com.google.common.collect.Maps.newHashMap;
import static java.lang.Thread.currentThread;

/**
 * @author Rick
 */
public final class SessionRepository {
    private static final Map<Integer, Session> sessionMap = newHashMap();
    private static final Map<ThreadGroup, Session> groupMap = newHashMap();

    private SessionRepository() {
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
}
