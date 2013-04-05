package ms.aurora.api.wrappers;

import ms.aurora.api.Context;
import ms.aurora.api.methods.Viewport;
import ms.aurora.api.methods.Walking;
import ms.aurora.rt3.GameObject;
import ms.aurora.rt3.GroundDecoration;
import ms.aurora.rt3.Model;

import java.awt.*;

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

    public final RSTile getLocation() {
        return new RSTile(getX(), getY());
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
        if(!Viewport.tileOnScreen(getLocation())) {
            Walking.walkTo(getLocation());
            return false;
        }
        Point screen = getClickLocation();
        ctx.input.getMouse().moveMouse(screen.x, screen.y);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return ms.aurora.api.methods.Menu.click(actionName);
    }

    public final boolean hover() {
        if(!Viewport.tileOnScreen(getLocation())) {
            Walking.walkTo(getLocation());
            return false;
        }
        Point screen = getScreenLocation();
        ctx.input.getMouse().moveMouse(screen.x, screen.y);
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
        if(!Viewport.tileOnScreen(getLocation())) {
            Walking.walkTo(getLocation());
            return false;
        }
        Point screen = getClickLocation();
        ctx.input.getMouse().clickMouse(screen.x, screen.y, left);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return true;
    }

    private Point getClickLocation() {
        return getModel().getRandomPoint();
    }


    public RSModel getModel() {
        if (wrapped.getModel() != null && wrapped.getModel() instanceof Model) {
            return new RSModel(ctx, (Model) wrapped.getModel(), getLocalX(), getLocalY(),
                    wrapped instanceof GroundDecoration ? 0 : wrapped.getOrientation());
        }
        return null;
    }

}
