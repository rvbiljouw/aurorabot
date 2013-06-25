package ms.aurora.api.wrappers;

import ms.aurora.api.Context;
import ms.aurora.api.methods.Menu;
import ms.aurora.api.methods.Objects;
import ms.aurora.api.methods.Players;
import ms.aurora.api.methods.Viewport;
import ms.aurora.api.pathfinding.Path;
import ms.aurora.api.pathfinding.impl.RSMapPathFinder;
import ms.aurora.api.util.Utilities;
import ms.aurora.input.VirtualMouse;
import ms.aurora.input.action.impl.MouseMovedAction;
import ms.aurora.rt3.*;
import org.apache.log4j.Logger;

import java.awt.*;

import static ms.aurora.api.methods.Menu.contains;
import static ms.aurora.api.methods.Menu.containsPred;

/**
 * @author Rick
 */
public final class GameObject implements Locatable, Interactable {
    private static final Logger logger = Logger.getLogger(GameObject.class);
    private final IGameObject wrapped;
    private ObjectType objectType = ObjectType.NULL;
    private Model cachedModel;
    private int localX;
    private int localY;

    public GameObject(IGameObject wrapped, int localX, int localY) {
        this.wrapped = wrapped;
        this.localX = localX;
        this.localY = localY;
        if (wrapped instanceof IGroundDecoration) {
            objectType = ObjectType.GROUND_DECORATION;
        } else if (wrapped instanceof IWallDecoration) {
            objectType = ObjectType.WALL_DECORATION;
        } else if (wrapped instanceof IWallObject) {
            objectType = ObjectType.WALL_OBJECT;
        } else {
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
        return (getLocalX() >> 7) + Context.getClient().getBaseX();
    }

    public final int getY() {
        return (getLocalY() >> 7) + Context.getClient().getBaseY();
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
        RSMapPathFinder pf = new RSMapPathFinder();
        Path path = pf.getPath(getX(), getY(), RSMapPathFinder.FULL);
        return path != null && path.getLength() > 0;
    }

    public final Tile getLocation() {
        return new Tile(getX(), getY(), 0);
    }

    public final Tile getRegionalLocation() {
        int x = getLocalX();
        int y = getLocalY();
        return new Tile(x, y, 0);
    }

    public final int distance(Locatable other) {
        return (int) Point.distance(getX(), getY(), other.getX(), other.getY());
    }

    /**
     * @param actionName
     * @return
     */
    public final boolean applyAction(String actionName) {
        if (!Viewport.tileOnScreen(getLocation()) && !Viewport.contains(getClickLocation())) {
            logger.info("Object not on screen");
            return false;
        }

        /*Point click = getClickLocation();
        VirtualMouse.moveMouse(click.x, click.y);*/
        final Tile location = Players.getLocal().getLocation();
        new MouseMovedAction() {

            @Override
            public Point getTarget() {
                return GameObject.this.getClickLocation();
            }

            @Override
            public boolean canStep() {
                return Players.getLocal().getLocation().equals(location);
            }
        }.apply();
        Utilities.sleepUntil(containsPred(actionName), 400);
        boolean success = contains(actionName) && Menu.click(actionName);
        if (success && cachedModel != null) {
            cachedModel.cleanup();
            //System.gc();
        }
        return success;
    }

    public final boolean hover() {
        if (!Viewport.tileOnScreen(getLocation())) {
            return false;
        }
        /*Point screen = getScreenLocation();
        VirtualMouse.moveMouse(screen.x, screen.y);*/
        final Tile location = Players.getLocal().getLocation();
        new MouseMovedAction() {

            @Override
            public Point getTarget() {
                return GameObject.this.getClickLocation();
            }

            @Override
            public boolean canStep() {
                return Players.getLocal().getLocation().equals(location);
            }
        }.apply();
        return true;
    }

    @Override
    public final boolean click(final boolean left) {
        if (!Viewport.tileOnScreen(getLocation())) {
            return false;
        }
        /*oint screen = getClickLocation();
        VirtualMouse.clickMouse(screen.x, screen.y, left);*/
        final Tile location = Players.getLocal().getLocation();
        new MouseMovedAction() {

            @Override
            public Point getTarget() {
                return GameObject.this.getClickLocation();
            }

            @Override
            public boolean canStep() {
                return Players.getLocal().getLocation().equals(location);
            }

            @Override
            public void end() {
                VirtualMouse.clickMouse(left);
            }
        }.apply();
        return true;
    }

    @Override
    public Point getClickLocation() {
        try {
            if (getModel() != null) {
                return getModel().getRandomPoint();
            }
        } catch (Exception ignored) {
        }
        return getScreenLocation();
    }

    public Model getModel() {
        if (wrapped.getModel() != null && wrapped.getModel() instanceof IModel) {
            if (cachedModel == null)
                cachedModel = new Model((IModel) wrapped.getModel(), getLocalX(), getLocalY(), 0);
            return cachedModel;
        }
        return null;
    }

    public boolean isValid() {
        GameObject[] objectsAtPos = Objects.getObjectsAtLocal(localX, localY).toArray(new GameObject[0]);
        for (GameObject object : objectsAtPos) {
            if (object.getId() == getId()) return true;
        }
        return false;
    }

    @Override
    public String toString() {
        return getId() + " | " + objectType.name();
    }

    @Override
    public int hashCode() {
        return getId() * (getX() + getY());
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof GameObject)) {
            return false;
        }
        return obj.hashCode() == hashCode();
    }

    public static enum ObjectType {GROUND_DECORATION, WALL_DECORATION, WALL_OBJECT, ANIMABLE, NULL}
}
