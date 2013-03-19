package ms.aurora.api.wrappers;

import ms.aurora.api.ClientContext;
import ms.aurora.api.rt3.Npc;

/**
 * @author rvbiljouw
 */
public final class RSNPC extends RSCharacter {
    private final Npc wrapped;

    public RSNPC(ClientContext clientContext, ms.aurora.api.rt3.Npc wrapped) {
        super(clientContext, wrapped);
        this.wrapped = wrapped;
    }

    public int getId() {
        if (wrapped != null && wrapped.getComposite() != null) {
            return wrapped.getComposite().getId();
        }
        return -1;
    }

    public String getName() {
        if (wrapped != null && wrapped.getComposite() != null) {
            return wrapped.getComposite().getName();
        }
        return "undefined";
    }

}
                                                        