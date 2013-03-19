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
    public static final ThreadLocal<ClientContext> context = new ThreadLocal<ClientContext>();

    public final InputManager input;
    private Session session;

    public ClientContext() {
        input = new InputManager(this);
    }

    public final void setSession(Session session) {
        this.session = session;
    }

    public final Session getSession() {
        return session;
    }

    public final RSPlayer getMyPlayer() {
        Player player = getClient().getLocalPlayer();
        if (player != null) {
            return new RSPlayer(this, player);
        }
        return null;
    }

    public final Client getClient() {
        return (Client) session.getApplet();
    }

    public static ClientContext get() {
        return context.get();
    }

    public static void set(ClientContext aContext) {
        context.set(aContext);
    }
}
