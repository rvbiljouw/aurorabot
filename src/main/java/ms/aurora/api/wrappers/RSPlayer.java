package ms.aurora.api.wrappers;

import ms.aurora.api.Context;
import ms.aurora.rt3.Player;

/**
 * @author rvbiljouw
 */
public final class RSPlayer extends RSCharacter {
    private final Player wrapped;

    public RSPlayer(Context contextContext, Player wrapped) {
        super(contextContext, wrapped);
        this.wrapped = wrapped;
    }

    public String getName() {
        return wrapped.getName();
    }
}
