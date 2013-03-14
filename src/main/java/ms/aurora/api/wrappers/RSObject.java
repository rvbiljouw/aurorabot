package ms.aurora.api.wrappers;

import ms.aurora.api.*;
import ms.aurora.api.rt3.GameObject;

import java.awt.*;

/**
 * @author rvbiljouw
 */
public class RSObject implements Locatable {
    private final ClientContext context;
    private final GameObject wrapped;
    private int localX;
    private int localY;

    public RSObject(ClientContext context, GameObject wrapped) {
        this.context = context;
        this.wrapped = wrapped;
        this.localX = localX;
        this.localY = localY;
    }

    public int getId() {
        return wrapped.getId() >> 14 & 0x7fff;
    }

    public int getLocalX() {
        return localX;
    }

    public int getLocalY() {
        return localY;
    }

    public int getX() {
        return context.getClient().getBaseX() + getLocalX();
    }

    public int getY() {
        return context.getClient().getBaseY() + getLocalY();
    }

    public Point getScreenLocation() {
        return Projection.worldToScreen(getRegionalLocation());
    }

    public RSTile getLocation() {
        return new RSTile(getX(), getY());
    }

    public RSTile getRegionalLocation() {
        int x = getLocalX() * 128;
        int z = getLocalY() * 128;
        return new RSTile(x, z, 0);
    }

    public int distance(Locatable other) {
        return (int) Point.distance(getX(), getY(), other.getX(), other.getY());
    }

    /**
     * @param actionName
     * @return
     */
    public boolean applyAction(String actionName) {
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

    public boolean hover() {
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
}
