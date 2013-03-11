package ms.aurora.api.wrappers;

import ms.aurora.api.ClientContext;
import ms.aurora.api.rt3.Player;

/**
 * @author rvbiljouw
 */
public class RSPlayer extends RSCharacter {
    private final Player wrapped;

    public RSPlayer(ClientContext clientContextContext, Player wrapped) {
        super(clientContextContext, wrapped);
        this.wrapped = wrapped;
    }

    public String getName() {
        return wrapped.getName();
    }
}
