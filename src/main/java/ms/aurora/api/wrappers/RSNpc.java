package ms.aurora.api.wrappers;

import ms.aurora.api.ClientContext;
import ms.aurora.api.rt3.Npc;

/**
 * @author rvbiljouw
 */
public class RSNPC extends RSCharacter {
    private final Npc wrapped;

    public RSNPC(ClientContext clientContext, Npc wrapped) {
        super(clientContext, wrapped);
        this.wrapped = wrapped;
    }

    public int getId() {
        return wrapped.getComposite().getId();
    }

    public String getName() {
        return wrapped.getComposite().getName();
    }
}
