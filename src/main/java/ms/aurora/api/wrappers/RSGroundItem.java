package ms.aurora.api.wrappers;

import ms.aurora.api.Context;
import ms.aurora.api.methods.*;
import ms.aurora.api.pathfinding.Path;
import ms.aurora.api.pathfinding.impl.RSMapPathFinder;
import ms.aurora.api.util.Utilities;
import ms.aurora.input.VirtualMouse;
import ms.aurora.rt3.Item;

import java.awt.*;

import static ms.aurora.api.Context.getProperty;
import static ms.aurora.api.methods.Menu.contains;
import static ms.aurora.api.methods.Menu.containsPred;

/**
 * Wrapper for ground items.
 *
 * @author tobiewarburton
 */
public final class RSGroundItem implements Locatable, Interactable {
    private Item item;
    private int localX;
    private int localY;
    private int z;

    public RSGroundItem(Item item, int localX, int localY, int z) {
        this.item = item;
        this.localX = localX;
        this.localY = localY;
        this.z = z;
    }

    public int getId() {
        return item.getId();
    }

    public int getStackSize() {
        return item.getStackSize();
    }

    public Point getScreenLocation() {
        return Viewport.convertLocal(new RSTile(getLocalX() * 128 + 64, getLocalY() * 128 + 64, z));
    }

    public RSTile getLocation() {
        return new RSTile(getX(), getY(), z);
    }

    public RSTile getRegionalLocation() {
        return new RSTile(getLocalX(), getLocalY(), z);
    }

    public int distance(Locatable other) {
        return (int) Point.distance(getX(), getY(), other.getX(), other.getY());
    }

    public int getX() {
        return getLocalX() + Context.getClient().getBaseX();
    }

    public int getY() {
        return getLocalY() + Context.getClient().getBaseY();
    }

    @Override
    public boolean isOnScreen() {
        return Viewport.tileOnScreen(getLocation());
    }

    @Override
    public boolean canReach() {
        RSMapPathFinder pf = new RSMapPathFinder();
        Path path = pf.getPath(getX(), getY(), RSMapPathFinder.FULL);
        return path != null && path.getLength() > 0;
    }

    public int getLocalX() {
        return localX;
    }

    public int getLocalY() {
        return localY;
    }

    /**
     * @param actionName
     * @return
     */
    @Override
    public boolean applyAction(String actionName) {
        if (!Viewport.tileOnScreen(getLocation()) && !Viewport.contains(getScreenLocation())) {
            return false;
        }

        Point click = getScreenLocation();
        VirtualMouse.moveMouse(click.x, click.y);
        Utilities.sleepUntil(containsPred(actionName), 400);
        return contains(actionName) && ms.aurora.api.methods.Menu.click(actionName);
    }

    @Override
    public boolean hover() {
        if (!Viewport.tileOnScreen(getLocation())) {
            return false;
        }
        Point screen = getScreenLocation();
        if (screen.x == -1 && screen.y == -1) return false;
        VirtualMouse.moveMouse(screen.x, screen.y);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public boolean click(boolean left) {
        if (!Viewport.tileOnScreen(getLocation())) {
            if (getProperty("interaction.walkTo").equals("true")) {
                Walking.clickOnMap(getLocation());
            }
            return false;
        }
        Point screen = getScreenLocation();
        if (screen.x == -1 && screen.y == -1) return false;
        VirtualMouse.clickMouse(screen.x, screen.y, left);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return true;
    }
}
