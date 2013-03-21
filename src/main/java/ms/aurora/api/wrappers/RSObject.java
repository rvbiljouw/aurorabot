package ms.aurora.api.wrappers;

import ms.aurora.api.*;
import ms.aurora.api.Projection;
import ms.aurora.api.rt3.GameObject;

import java.awt.*;

/**
 * @author rvbiljouw
 */
public final class RSObject implements Locatable, Interactable {
    private final ClientContext context;
    private final GameObject wrapped;
    private int localX;
    private int localY;

    public RSObject(ClientContext context, GameObject wrapped, int localX, int localY) {
        this.context = context;
        this.wrapped = wrapped;
        this.localX = localX;
        this.localY = localY;
    }

    public final int getId() {
        return wrapped.getHash() >> 14 & 0x7fff;
    }

    public final int getLocalX() {
        return localX * 128;
    }

    public final int getLocalY() {
        return localY * 128;
    }

    public final int getX() {
        return (wrapped.getX() >> 7) + context.getClient().getBaseX();
    }

    public final int getY() {
        return (wrapped.getY() >> 7) + context.getClient().getBaseY();
    }

    public final Point getScreenLocation() {
        return Projection.worldToScreen(getRegionalLocation());
    }

    public final RSTile getLocation() {
        return new RSTile(getX(), getY());
    }

    public final RSTile getRegionalLocation() {
        int x = getLocalX();
        int y = getLocalY();
        return new RSTile(x, y, wrapped.getZ());
    }

    public final int distance(Locatable other) {
        return (int) Point.distance(getX(), getY(), other.getX(), other.getY());
    }

    /**
     * @param actionName
     * @return
     */
    public final boolean applyAction(String actionName) {
        if (!Projection.tileOnScreen(getRegionalLocation()))
            return false;
        Point screen = getScreenLocation();
        context.input.getMouse().moveMouse(screen.x, screen.y);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return ms.aurora.api.Menu.click(actionName);
    }

    public final boolean hover() {
        if (!Projection.tileOnScreen(getRegionalLocation()))
            return false;
        Point screen = getScreenLocation();
        context.input.getMouse().moveMouse(screen.x, screen.y);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public final boolean click(boolean left) {
        if (!Projection.tileOnScreen(getRegionalLocation()))
            return false;
        Point screen = getScreenLocation();
        context.input.getMouse().clickMouse(screen.x, screen.y, left);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return true;
    }
}
