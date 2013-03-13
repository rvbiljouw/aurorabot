package ms.aurora.api;

import ms.aurora.api.rt3.Client;
import ms.aurora.api.rt3.Player;
import ms.aurora.api.wrappers.RSPlayer;
import ms.aurora.core.Session;
import ms.aurora.input.InputManager;

/**
 * @author rvbiljouw
 */
public class ClientContext {
    public final InputManager input;
    public final Projection projection;
    public final Menu menu;
    public final Npcs npcs;
    private Session session;

    public ClientContext() {
        input = new InputManager(this);
        projection = new Projection(this);
        menu = new Menu(this);
        npcs = new Npcs(this);
    }

    public void setSession(Session session) {
        this.session = session;
    }

    public Session getSession() {
        return session;
    }

    public RSPlayer getMyPlayer() {
        Player player = getClient().getLocalPlayer();
        if (player != null) {
            return new RSPlayer(this, player);
        }
        return null;
    }

    public Client getClient() {
        return (Client) session.getApplet();
    }

}
