package ms.aurora.api;

import ms.aurora.api.wrappers.RSPlayer;

/**
 * @author rvbiljouw
 */
public class Players {

    public static RSPlayer getLocal() {
        return ClientContext.get().getMyPlayer();
    }

}
