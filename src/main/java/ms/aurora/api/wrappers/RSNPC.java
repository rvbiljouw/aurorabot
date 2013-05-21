package ms.aurora.api.wrappers;

import ms.aurora.rt3.Npc;

/**
 * @author Rick
 */
public final class RSNPC extends RSCharacter {
    private final Npc wrapped;

    public RSNPC(Npc wrapped) {
        super(wrapped);
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
                                                        