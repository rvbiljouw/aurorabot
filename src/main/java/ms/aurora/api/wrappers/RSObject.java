package ms.aurora.api.wrappers;

import ms.aurora.api.Context;
import ms.aurora.api.methods.Objects;
import ms.aurora.api.methods.Viewport;
import ms.aurora.api.methods.Walking;
import ms.aurora.api.pathfinding.Path;
import ms.aurora.api.pathfinding.impl.RSPathFinder;
import ms.aurora.input.VirtualMouse;
import ms.aurora.rt3.*;

import java.awt.*;

/**
 * @author Rick
 */
public final class RSObject implements Locatable, Interactable {
    private final Context ctx;
    private final GameObject wrapped;

    private ObjectType objectType = ObjectType.NULL;
    private int localX;
    private int localY;

    public static enum ObjectType { GROUND_DECORATION, WALL_DECORATION, WALL_OBJECT, ANIMABLE, NULL }

    public RSObject(Context ctx, GameObject wrapped, int localX, int localY) {
        this.ctx = ctx;
        this.wrapped = wrapped;
        this.localX = localX;
        this.localY = localY;
        if (wrapped instanceof GroundDecoration) {
            objectType = ObjectType.GROUND_DECORATION;
        } else if (wrapped instanceof WallDecoration) {
            objectType = ObjectType.WALL_DECORATION;
        } else if (wrapped instanceof WallObject) {
            objectType = ObjectType.WALL_OBJECT;
        } else if (wrapped instanceof AnimableObject) {
            objectType = ObjectType.ANIMABLE;
        }
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

    public ObjectType getObjectType() {
        return objectType;
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
            Walking.clickOnMap(getLocation());
            return false;
        }

        for (int attempt = 0; attempt < 10; attempt++) {
            Point click = getClickLocation();
            VirtualMouse.moveMouse(click.x, click.y);
            if (ms.aurora.api.methods.Menu.getIndex(actionName) != -1) {
                return ms.aurora.api.methods.Menu.click(actionName);
            }
        }
        return false;
    }

    public final boolean hover() {
        Point screen = getScreenLocation();
        if (screen.x != -1 && screen.y != -1) {
            VirtualMouse.moveMouse(screen.x, screen.y);
            return true;
        }
        return false;
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
            return new RSModel(ctx, (Model) wrapped.getModel(), getLocalX(), getLocalY(), 0);
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

    @Override
    public String toString() {
        return getId() + " | " + (wrapped instanceof GroundDecoration ? 0 : wrapped.getOrientation());
    }
}
