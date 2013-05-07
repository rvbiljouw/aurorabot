package ms.aurora.api.wrappers;

import ms.aurora.api.Context;
import ms.aurora.api.methods.Skills;
import ms.aurora.rt3.Player;

/**
 * @author Rick
 */
public final class RSPlayer extends RSCharacter {
    private final Player wrapped;

    public RSPlayer(Context contextContext, Player wrapped) {
        super(contextContext, wrapped);
        this.wrapped = wrapped;
    }

    public final String getName() {
        return wrapped.getName();
    }

    @Override
    public final int getCurrentHealth() {
        return Skills.getLevel(Skills.Skill.HITPOINTS);
    }

    @Override
    public final int getMaxHealth() {
        return Skills.getBaseLevel(Skills.Skill.HITPOINTS);
    }
}
