package ms.aurora.api.wrappers;

import ms.aurora.api.ClientContext;
import ms.aurora.api.Players;
import ms.aurora.api.Projection;
import ms.aurora.api.rt3.Model;

import java.awt.*;

import static ms.aurora.api.util.Utilities.sleepNoException;


/**
 * @author rvbiljouw
 */
public class RSCharacter extends RSRenderable implements Locatable {
    private final ms.aurora.api.rt3.Character wrapped;

    public RSCharacter(ClientContext clientContext,
                       ms.aurora.api.rt3.Character wrapped) {
        super(clientContext, wrapped);
        this.wrapped = wrapped;
    }

    public Point getScreenLocation() {
        return Projection.worldToScreen(getRegionalLocation());
    }

    public RSTile getLocation() {
        return new RSTile(getX(), getY());
    }

    public RSTile getRegionalLocation() {
        int x = getLocalX();
        int z = getLocalY();
        return new RSTile(x, z, -(wrapped.getModelHeight() / 2));
    }

    public int distance(Locatable other) {
        return (int) Point.distance(getX(), getY(), other.getX(), other.getY());
    }

    public int getX() {
        return context.getClient().getBaseX() + (getLocalX() >> 7);
    }

    public int getY() {
        return context.getClient().getBaseY() + (getLocalY() >> 7);
    }

    public int getLocalX() {
        return wrapped.getLocalX();
    }

    public int getLocalY() {
        return wrapped.getLocalY();
    }

    /**
     * @return the character that the current character is interacting with, or
     *         null
     */
    public RSCharacter getInteracting() {
        int interacting = getInteractingEntity();
        if (interacting == -1) {
            return null;
        } else if (interacting < 32767) {
            return new RSNPC(context,
                    context.getClient().getAllNpcs()[interacting]);
        } else if (interacting >= 32767) {
            int index = (interacting - 32767);
            return new RSPlayer(context,
                    context.getClient().getAllPlayers()[index]);
        }
        return null;
    }

    /**
     * @return if the current character is in combat
     */
    public boolean isInCombat() {
        return context.getClient().getLoopCycle() < getLoopCycleStatus();
    }

    /**
     * @param actionName
     * @return
     */
    public void applyAction(final String actionName) {
        Point screen = getScreenLocation();
        context.input.getMouse().moveMouse(screen.x, screen.y);
        ms.aurora.api.Menu.click(actionName);
        sleepNoException(700);

        while(Players.getLocal().isMoving()) {
            sleepNoException(600);
        }
    }

    public boolean hover() {
        if (!Projection.tileOnScreen(getRegionalLocation())) {
            return false;
        }
        Point screen = getScreenLocation();
        context.input.getMouse().moveMouse(screen.x, screen.y);
        return true;
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

    public boolean isMoving() {
        return wrapped.getPathLength() != 0;
    }

    public RSModel getModel() {
        Model model = _getModel();
        if(model != null) {
            return new RSModel(model, getLocalX(),  getLocalY(),  getTurnDirection());
        }
        return null;
    }

    public String dbgString() {
        return "Path: " + wrapped.getPathLength() + " IC: " + isInCombat();
    }
}
