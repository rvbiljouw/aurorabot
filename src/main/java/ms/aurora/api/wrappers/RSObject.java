package ms.aurora.api.wrappers;

import ms.aurora.api.Context;
import ms.aurora.api.methods.Objects;
import ms.aurora.api.methods.Viewport;
import ms.aurora.api.methods.Walking;
import ms.aurora.api.pathfinding.Path;
import ms.aurora.api.pathfinding.impl.RSPathFinder;
import ms.aurora.input.VirtualMouse;
import ms.aurora.rt3.GameObject;
import ms.aurora.rt3.GroundDecoration;
import ms.aurora.rt3.Model;

import java.awt.*;

import static ms.aurora.api.util.Utilities.sleepNoException;

/**
 * @author Rick
 */
public final class RSObject implements Locatable, Interactable {
    private final Context ctx;
    private final GameObject wrapped;
    private int localX;
    private int localY;

    public RSObject(Context ctx, GameObject wrapped, int localX, int localY) {
        this.ctx = ctx;
        this.wrapped = wrapped;
        this.localX = localX;
        this.localY = localY;
    }

    public final int getId() {
        return wrapped.getHash() >> 14 & 32767;
    }

    public final int getLocalX() {
        return wrapped.getX();
    }

    public final int getLocalY() {
        return wrapped.getY();
    }

    public final int getX() {
        return (getLocalX() >> 7) + ctx.getClient().getBaseX();
    }

    public final int getY() {
        return (getLocalY() >> 7) + ctx.getClient().getBaseY();
    }

    public final Point getScreenLocation() {
        return Viewport.convertLocal(getRegionalLocation());
    }

    public final boolean isOnScreen() {
        Point screenLocation = this.getScreenLocation();
        return screenLocation.x != -1 && screenLocation.y != -1;
    }

    public boolean canReach() {
        RSPathFinder pf = new RSPathFinder();
        Path path = pf.getPath(getX(), getY(), RSPathFinder.FULL);
        return path != null && path.getLength() > 0;
    }

    public final RSTile getLocation() {
        return new RSTile(getX(), getY(), 0);
    }

    public final RSTile getRegionalLocation() {
        int x = getLocalX();
        int y = getLocalY();
        return new RSTile(x, y, 0);
    }

    public final int distance(Locatable other) {
        return (int) Point.distance(getX(), getY(), other.getX(), other.getY());
    }

    /**
     * @param actionName
     * @return
     */
    public final boolean applyAction(String actionName) {
        if (!Viewport.tileOnScreen(getLocation())) {
            Walking.walkTo(getLocation());
            return false;
        }
        Point click = getClickLocation();
        Point screen = getScreenLocation();
        VirtualMouse.moveMouse(click.x, click.y);
        sleepNoException(200, 300);
        return isValid() && screen.distance(getScreenLocation()) < 10 && ms.aurora.api.methods.Menu.click(actionName);
    }

    public final boolean hover() {
        if (!Viewport.tileOnScreen(getLocation())) {
            Walking.walkTo(getLocation());
            return false;
        }
        Point screen = getScreenLocation();
        VirtualMouse.moveMouse(screen.x, screen.y);
        return true;
    }

    @Override
    public final boolean click(boolean left) {
        if (!Viewport.tileOnScreen(getLocation())) {
            Walking.walkTo(getLocation());
            return false;
        }
        Point screen = getClickLocation();
        VirtualMouse.clickMouse(screen.x, screen.y, left);
        return true;
    }

    private Point getClickLocation() {
        try {
            if (getModel() != null) {
                return getModel().getRandomPoint();
            }
        } catch (Exception e) {
            //  e.printStackTrace();
        }
        return getScreenLocation();
    }

    public RSModel getModel() {
        if (wrapped.getModel() != null && wrapped.getModel() instanceof Model) {
            return new RSModel(ctx, (Model) wrapped.getModel(), getLocalX(), getLocalY(),
                    wrapped instanceof GroundDecoration ? 0 : wrapped.getOrientation());
        }
        return null;
    }

    private boolean isValid() {
        RSObject[] objectsAtPos = Objects.getObjectsAt(localX, localY).toArray(new RSObject[0]);
        for (RSObject object : objectsAtPos) {
            if (object.getId() == getId()) return true;
        }
        return false;
    }
}
