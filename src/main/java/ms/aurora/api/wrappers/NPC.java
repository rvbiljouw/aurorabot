package ms.aurora.api.wrappers;

import ms.aurora.rt3.INpc;

/**
 * @author Rick
 */
public final class NPC extends Entity {
    private final INpc wrapped;

    public NPC(INpc wrapped) {
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
                                                        