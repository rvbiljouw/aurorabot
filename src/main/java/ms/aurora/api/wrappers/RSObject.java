package ms.aurora.api.wrappers;

import ms.aurora.api.ClientContext;
import ms.aurora.api.Projection;
import ms.aurora.api.rt3.GameObject;

import java.awt.*;

/**
 * @author rvbiljouw
 */
public class RSObject implements Locatable {
    private final ClientContext context;
    private final GameObject wrapped;

    public RSObject(ClientContext context, GameObject wrapped) {
        this.context = context;
        this.wrapped = wrapped;
    }

    public int getId() {
        return wrapped.getHash() >> 14 & 0x7fff;
    }

    public int getLocalX() {
        return wrapped.getX();
    }

    public int getLocalY() {
        return wrapped.getY();
    }

    public int getX() {
        return (wrapped.getX() >> 7) + context.getClient().getBaseX();
    }

    public int getY() {
        return (wrapped.getY() >> 7) + context.getClient().getBaseY();
    }

    public Point getScreenLocation() {
        return Projection.worldToScreen(getRegionalLocation());
    }

    public RSTile getLocation() {
        return new RSTile(getX(), getY());
    }

    public RSTile getRegionalLocation() {
        int x = getLocalX();
        int y = getLocalY();
        return new RSTile(x, y, wrapped.getZ());
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
