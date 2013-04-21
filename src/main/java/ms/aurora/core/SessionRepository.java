package ms.aurora.core;

import org.jboss.logging.Logger;

import java.util.Collection;
import java.util.Map;

import static com.google.common.collect.Maps.newHashMap;
import static java.lang.Thread.currentThread;

/**
 * @author Rick
 */
public final class SessionRepository {
    private static final Logger logger = Logger.getLogger(SessionRepository.class);
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
        logger.info("Stored session from group " + currentThread().getThreadGroup().getName());
        groupMap.put(currentThread().getThreadGroup(), session);
        return sessionMap.put(appletHash, session);
    }

    public static Collection<Session> getAll() {
        return sessionMap.values();
    }
}
