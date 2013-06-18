package ms.aurora.api.wrappers;

import ms.aurora.api.methods.Skill;
import ms.aurora.rt3.IPlayer;

/**
 * @author Rick
 */
public final class Player extends Entity {
    private final IPlayer wrapped;

    public Player(IPlayer wrapped) {
        super(wrapped);
        this.wrapped = wrapped;
    }

    public final String getName() {
        return wrapped.getName();
    }

    @Override
    public final int getCurrentHealth() {
        return Skill.HITPOINTS.getLevel();
    }

    @Override
    public final int getMaxHealth() {
        return Skill.HITPOINTS.getBaseLevel();
    }
}
