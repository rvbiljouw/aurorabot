package ms.aurora.api.wrappers;

import ms.aurora.api.ClientContext;
import ms.aurora.api.rt3.Model;

import java.awt.*;

import static ms.aurora.api.util.Utilities.sleepNoException;


/**
 * @author rvbiljouw
 */
public class RSCharacter extends RSRenderable implements Locatable, Interactable {
    private final ms.aurora.api.rt3.Character wrapped;

    public RSCharacter(ClientContext clientContext,
                       ms.aurora.api.rt3.Character wrapped) {
        super(clientContext, wrapped);
        this.wrapped = wrapped;
    }

    public final Point getScreenLocation() {
        return ctx.projection.worldToScreen(getRegionalLocation());
    }

    public final RSTile getLocation() {
        return new RSTile(getX(), getY());
    }

    public final RSTile getRegionalLocation() {
        int x = getLocalX();
        int z = getLocalY();
        //return new RSTile(x, z, -(wrapped.getModelHeight() / 2));
        return new RSTile(x, z, -(wrapped.getHeight() / 2));
    }

    public final int distance(Locatable other) {
        return (int) Point.distance(getX(), getY(), other.getX(), other.getY());
    }

    public final int getX() {
        return ctx.getClient().getBaseX() + (getLocalX() >> 7);
    }

    public final int getY() {
        return ctx.getClient().getBaseY() + (getLocalY() >> 7);
    }

    public final int getLocalX() {
        return wrapped.getLocalX();
    }

    public final int getLocalY() {
        return wrapped.getLocalY();
    }

    /**
     * @return the character that the current character is interacting with, or
     *         null
     */
    public final RSCharacter getInteracting() {
        int interacting = getInteractingEntity();
        if (interacting == -1) {
            return null;
        } else if (interacting < 32767) {
            return new RSNPC(ctx,
                    ctx.getClient().getAllNpcs()[interacting]);
        } else if (interacting >= 32767) {
            int index = (interacting - 32767);
            return new RSPlayer(ctx,
                    ctx.getClient().getAllPlayers()[index]);
        }
        return null;
    }

    /**
     * @return if the current character is in combat
     */
    public final boolean isInCombat() {
        return ctx.getClient().getLoopCycle() < getLoopCycleStatus();
    }

    /**
     * @param actionName
     * @return
     */
    public final boolean applyAction(final String actionName) {
        Point screen = getScreenLocation();
        ctx.input.getMouse().moveMouse(screen.x, screen.y);
        ctx.menu.click(actionName);
        sleepNoException(700);

        while(ctx.players.getLocal().isMoving()) {
            sleepNoException(600);
        }
        return true;
    }

    @Override
    public final boolean hover() {
        if (!ctx.projection.tileOnScreen(getRegionalLocation())) {
            return false;
        }
        //Point screen = getScreenLocation();
        Point screen = this.getModel().getRandomHullPoint();
        if (screen == null) return false;
        System.out.println(screen);
        ctx.input.getMouse().moveMouse(screen.x, screen.y);
        return true;
    }

    @Override
    public final boolean click(boolean left) {
        Point screen = getScreenLocation();
        ctx.input.getMouse().clickMouse(screen.x, screen.y, left);
        return false;
    }

    public final int getHitsLoopCycle() {
        return wrapped.getHitsLoopCycle();
    }

    public final int getAnimation() {
        return wrapped.getAnimation();
    }

    public final int getCurrentHealth() {
        return wrapped.getCurrentHealth();
    }

    public final int getMaxHealth() {
        return wrapped.getMaxHealth();
    }

    public final int getLoopCycleStatus() {
        return wrapped.getLoopCycleStatus();
    }

    public final int getTurnDirection() {
        return Math.abs(wrapped.getTurnDirection() - 1024);
    }

    public final String getMessage() {
        return wrapped.getMessage();
    }

    public final int getInteractingEntity() {
        return wrapped.getInteractingEntity();
    }

    public final boolean isMoving() {
        return wrapped.getPathLength() != 0;
    }

    public final RSModel getModel() {
        Model model = _getModel();
        if(model != null) {
            return new RSModel(ctx, model, getLocalX(),  getLocalY(),  getTurnDirection());
        }
        return null;
    }

    public final String dbgString() {
        return "Path: " + wrapped.getPathLength() + " IC: " + isInCombat();
    }
}
