package ms.aurora.api;

import ms.aurora.core.Session;
import ms.aurora.core.SessionRepository;
import ms.aurora.input.InputManager;
import ms.aurora.rt3.Client;

import static java.lang.Thread.currentThread;

/**
 * @author rvbiljouw
 */
public class Context {
    public final InputManager input;
    private ThreadGroup threadGroup;
    private Session session;

    public Context(Session session) {
        this.input = new InputManager(this);
        this.session = session;
    }

    public Context() {
        this.input = new InputManager(this);
    }

    public final Session getSession() {
        return session;
    }

    public final void setSession(Session session) {
        threadGroup = currentThread().getThreadGroup();
        this.session = session;
    }

    public final Client getClient() {
        if(session == null) {
            System.out.println(currentThread().getThreadGroup().getName());
        }
        return (Client) session.getApplet();
    }

    public static Context get() {
        return new Context(SessionRepository.get(currentThread().getThreadGroup()));
    }

    public ThreadGroup getThreadGroup() {
        return threadGroup;
    }
}
