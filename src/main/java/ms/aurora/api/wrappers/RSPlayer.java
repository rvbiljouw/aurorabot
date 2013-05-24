package ms.aurora.api.wrappers;

import ms.aurora.api.methods.Skill;
import ms.aurora.rt3.Player;

/**
 * @author Rick
 */
public final class RSPlayer extends RSCharacter {
    private final Player wrapped;

    public RSPlayer(Player wrapped) {
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
