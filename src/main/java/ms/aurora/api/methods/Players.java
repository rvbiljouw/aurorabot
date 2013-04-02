package ms.aurora.api.methods;

import ms.aurora.api.Context;
import ms.aurora.api.wrappers.RSPlayer;

/**
 * Player related functions
 * @author rvbiljouw
 */
public final class Players {

    /**
     * gets the local {@link RSPlayer}
     *
     * @return the local {@link RSPlayer}
     */
    public static RSPlayer getLocal() {
        Context ctx = Context.get();
        return new RSPlayer(ctx, ctx.getClient().getLocalPlayer());
    }


}
