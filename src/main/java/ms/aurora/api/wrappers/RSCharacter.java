package ms.aurora.api.wrappers;

import ms.aurora.api.Context;
import ms.aurora.api.methods.Minimap;
import ms.aurora.api.methods.Viewport;
import ms.aurora.api.methods.Walking;
import ms.aurora.api.pathfinding.Path;
import ms.aurora.api.pathfinding.impl.RSMapPathFinder;
import ms.aurora.input.VirtualMouse;
import ms.aurora.rt3.Model;
import org.apache.log4j.Logger;

import java.awt.*;

import static ms.aurora.api.Context.getProperty;
import static ms.aurora.input.VirtualMouse.moveMouse;


/**
 * @author Rick
 */
public class RSCharacter extends RSRenderable implements Locatable, Interactable {
    private static Logger logger = Logger.getLogger(RSCharacter.class);
    private final ms.aurora.rt3.Character wrapped;

    public RSCharacter(Context context,
                       ms.aurora.rt3.Character wrapped) {
        super(context, wrapped);
        this.wrapped = wrapped;
    }

    public final Point getScreenLocation() {
        return Viewport.convertLocal(getRegionalLocation());
    }

    private Point getClickLocation() {
        try {
            if (getModel() != null) {
                return getModel().getRandomPoint();
            }
        } catch (Exception e) {
            //  e.printStackTrace();
        }
        logger.debug("Couldn't find model, using screen location.");
        return getScreenLocation();
    }

    public final RSTile getLocation() {
        return new RSTile(getX(), getY(), -(wrapped.getHeight() / 2));
    }

    public final RSTile getRegionalLocation() {
        int x = getLocalX();
        int z = getLocalY();
        return new RSTile(x, z, -(wrapped.getHeight() / 2));
    }

    public final int distance(Locatable other) {
        return (int) Point.distance(getX(), getY(), other.getX(), other.getY());
    }

    public final int getX() {
        return Context.getClient().getBaseX() + (getLocalX() >> 7);
    }

    public final int getY() {
        return Context.getClient().getBaseY() + (getLocalY() >> 7);
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
                    Context.getClient().getAllNpcs()[interacting]);
        } else if (interacting >= 32767) {
            int index = (interacting - 32767);
            return new RSPlayer(ctx,
                    Context.getClient().getAllPlayers()[index]);
        }
        return null;
    }

    /**
     * @return if the current character is in combat
     */
    public final boolean isInCombat() {
        return Context.getClient().getLoopCycle() < getLoopCycleStatus();
    }

    /**
     * @param actionName
     * @return
     */
    public final boolean applyAction(final String actionName) {
        if (!Viewport.tileOnScreen(getLocation())) {
            return false;
        }
        Point click = getClickLocation();
        for (int attempt = 0; attempt < 10 && !Thread.currentThread().isInterrupted(); attempt++) {
            if (click.x <= 0 || click.y <= 0) continue;
            VirtualMouse.moveMouse(click.x, click.y);
            if (ms.aurora.api.methods.Menu.getIndex(actionName) != -1) {
                return ms.aurora.api.methods.Menu.click(actionName);
            }
        }
        return false;
    }

    @Override
    public final boolean hover() {
        if (!Viewport.tileOnScreen(getLocation())) {
            return false;
        }
        Point screen = getClickLocation();
        if (screen == null) return false;
        moveMouse(screen.x, screen.y);
        return true;
    }

    @Override
    public final boolean click(boolean left) {
        Point screen = getClickLocation();
        VirtualMouse.clickMouse(screen.x, screen.y, left);
        return false;
    }

    public final int getHitsLoopCycle() {
        return wrapped.getHitsLoopCycle();
    }

    public final int getAnimation() {
        return wrapped.getAnimation();
    }

    public final int getLoopCycleStatus() {
        return wrapped.getLoopCycleStatus();
    }

    public final int getTurnDirection() {
        return wrapped.getTurnDirection();
    }

    public final String getMessage() {
        return wrapped.getMessage() == null ? "" : wrapped.getMessage();
    }

    public final int getInteractingEntity() {
        return wrapped.getInteractingEntity();
    }

    public final boolean isMoving() {
        return wrapped.getPathLength() != 0;
    }

    public final boolean isOnScreen() {
        return this.getScreenLocation().getX() != -1 && this.getScreenLocation().getY() != -1;
    }

    public int getCurrentHealth() {
        return wrapped.getCurrentHealth();
    }

    public int getMaxHealth() {
        return wrapped.getMaxHealth();
    }

    public boolean canReach() {
        RSMapPathFinder pf = new RSMapPathFinder();
        Path path = pf.getPath(getX(), getY(), RSMapPathFinder.FULL);
        return path != null && path.getLength() > 0;
    }

    public final boolean isIdle() {
        return getAnimation() == -1 && !isMoving() && !isInCombat();
    }

    public final RSModel getModel() {
        Model model = _getModel();
        if (model != null) {
            return new RSModel(ctx, model, getLocalX(), getLocalY(), getTurnDirection());
        }
        return null;
    }

    public final String dbgString() {
        return "Path: " + wrapped.getPathLength() + " IC: " + isInCombat();
    }
}
