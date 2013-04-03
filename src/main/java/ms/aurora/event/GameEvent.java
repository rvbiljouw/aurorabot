package ms.aurora.event;


import ms.aurora.core.Session;

import java.util.EventListener;

/**
 * @author Rick
 */
public abstract class GameEvent<T> {

    private final transient T object;
    private final transient Class<? extends EventListener> listenerType;
    private Session session;

    public GameEvent(final T object, final Class<? extends EventListener> listenerType) {
        this.object = object;
        this.listenerType = listenerType;
    }

    public final T getObject() {
        return object;
    }

    public final void setSource(Session session) {
        this.session = session;
    }

    public final Session getSource() {
        return session;
    }

    public final Class<? extends EventListener> getListenerType() {
        return listenerType;
    }

    public abstract void dispatch(EventListener e);

}
