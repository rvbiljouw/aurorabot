package ms.aurora.api;

import ms.aurora.api.rt3.Client;
import ms.aurora.core.Session;

/**
 * @author rvbiljouw
 */
public class ClientContext {
    public final Projection projection;
    public final Npcs npcs;
    private Session session;

    public ClientContext() {
        projection = new Projection(this);
        npcs = new Npcs(this);
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public Session getSession() {
        return session;
    }

    public Client getClient() {
        return (Client) session.getApplet();
    }

}
