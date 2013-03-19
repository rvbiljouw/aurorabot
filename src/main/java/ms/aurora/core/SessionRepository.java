package ms.aurora.core;

import java.util.Collection;
import java.util.Map;

import static com.google.common.collect.Maps.newHashMap;

/**
 * @author rvbiljouw
 */
public class SessionRepository {
    private static final Map<Integer, Session> sessionMap = newHashMap();

    private SessionRepository() { }

    public static Session get(Integer appletHash) {
        return sessionMap.get(appletHash);
    }

    public static Session set(Integer appletHash, Session session) {
        return sessionMap.put(appletHash, session);
    }

    public static Collection<Session> getAll() {
        return sessionMap.values();
    }
}
