package ms.aurora.api.wrappers;

import ms.aurora.api.Context;
import ms.aurora.api.methods.Viewport;
import ms.aurora.api.methods.Walking;
import ms.aurora.api.pathfinding.Path;
import ms.aurora.api.pathfinding.impl.RSPathFinder;
import ms.aurora.input.VirtualMouse;
import ms.aurora.rt3.Item;

import java.awt.*;

/**
 * Wrapper for ground items.
 * @author tobiewarburton
 */
public final class RSGroundItem implements Locatable, Interactable {
    private Context ctx;
    private Item item;
    private int localX;
    private int localY;
    private int z;

    public RSGroundItem(Context ctx, Item item, int localX, int localY, int z) {
        this.ctx = ctx;
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
        return getLocalX() + ctx.getClient().getBaseX();
    }

    public int getY() {
        return getLocalY() + ctx.getClient().getBaseY();
    }

    @Override
    public boolean isOnScreen() {
        return Viewport.tileOnScreen(getLocation());
    }


    @Override
    public boolean canReach() {
        RSPathFinder pf = new RSPathFinder();
        Path path = pf.getPath(getX(), getY(), RSPathFinder.FULL);
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
        if (!Viewport.tileOnScreen(getLocation())) {
            //Walking.walkTo(getLocation());
            Walking.clickOnMap(getLocation());
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
        return ms.aurora.api.methods.Menu.click(actionName);
    }

    @Override
    public boolean hover() {
        if (!Viewport.tileOnScreen(getLocation())) {
            //Walking.walkTo(getLocation());
            Walking.clickOnMap(getLocation());
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
            //Walking.walkTo(getLocation());
            Walking.clickOnMap(getLocation());
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
