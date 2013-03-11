package ms.aurora.api.wrappers;

import ms.aurora.api.ClientContext;

import java.awt.*;

/**
 * @author rvbiljouw
 */
public class RSCharacter extends RSRenderable {
    private final ms.aurora.api.rt3.Character wrapped;

    public RSCharacter(ClientContext clientContext, ms.aurora.api.rt3.Character wrapped) {
        super(clientContext, wrapped);
        this.wrapped = wrapped;
    }

    public Point getScreenLocation() {
        return context.projection.worldToScreen(getRegionalLocation());
    }

    public RSTile getLocation() {
        int x = context.getClient().getBaseX() + (getLocalX() >> 7);
        int y = context.getClient().getBaseY() + (getLocalY() >> 7);
        return new RSTile(x, y);
    }

    public RSTile getRegionalLocation() {
        int x = getLocalX();
        int z = getLocalY();
        return new RSTile(x, z, getHeight());
    }


    public int getLocalX() {
        return wrapped.getLocalX();
    }


    public int getLocalY() {
        return wrapped.getLocalY();
    }

    /**
     * @return the character that the current character is interacting with, or null
     */
    public RSCharacter getInteracting() {
        int interacting = getInteractingEntity();
        if (interacting == -1) return null;
        if (interacting < 32767) {
            return new RSNpc(context, context.getClient().getAllNpcs()[interacting]);
        } else if (interacting >= 32767) {
            int index = (interacting - 32767);
            return new RSPlayer(context, context.getClient().getAllPlayers()[index]);
        }
        return null;
    }

    /**
     * @return if the current character is in combat
     */
    public boolean inCombat() {
        return context.getClient().getLoopCycle() < getLoopCycleStatus();
    }

    /**
     * @param actionName
     * @return
     */
    public boolean applyAction(String actionName) {
        if (!context.projection.tileOnScreen(getRegionalLocation()))
            return false;
        //TODO: need to sort out menu and stuff.
        return false;
    }


    public int getHitsLoopCycle() {
        return wrapped.getHitsLoopCycle();
    }

    public int getAnimation() {
        return wrapped.getAnimation();
    }

    public int getCurrentHealth() {
        return wrapped.getCurrentHealth();
    }

    public int getMaxHealth() {
        return wrapped.getMaxHealth();
    }


    public int getLoopCycleStatus() {
        return wrapped.getLoopCycleStatus();
    }


    public int getTurnDirection() {
        return wrapped.getTurnDirection();
    }


    public String getMessage() {
        return wrapped.getMessage();
    }


    public int getInteractingEntity() {
        return wrapped.getInteractingEntity();
    }

}
