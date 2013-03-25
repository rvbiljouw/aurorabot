package ms.aurora.api.methods;

import ms.aurora.api.ClientContext;
import ms.aurora.api.wrappers.RSPlayer;

/**
 * @author rvbiljouw
 */
public final class Players {
    private final ClientContext ctx;

    public Players(ClientContext ctx) {
        this.ctx = ctx;
    }

    /**
     * gets the local {@link RSPlayer}
     *
     * @return the local {@link RSPlayer}
     */
    public RSPlayer getLocal() {
        return new RSPlayer(ctx, ctx.getClient().getLocalPlayer());
    }

}
