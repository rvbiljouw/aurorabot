package ms.aurora.api;

import ms.aurora.api.wrappers.RSPlayer;

/**
 * @author rvbiljouw
 */
public class Players {

    /**
     * gets the local {@link RSPlayer}
     *
     * @return the local {@link RSPlayer}
     */
    public static RSPlayer getLocal() {
        return ClientContext.get().getMyPlayer();
    }

}
