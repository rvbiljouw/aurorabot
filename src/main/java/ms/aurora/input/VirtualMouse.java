package ms.aurora.input;

import ms.aurora.api.Context;
import ms.aurora.input.algorithm.BezierAlgorithm;
import ms.aurora.rt3.Mouse;
import org.apache.log4j.Logger;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

/**
 * this class will act like mouse
 *
 * @author Rick
 * @author tobiewarburton
 */
public final class VirtualMouse {
    private static final Logger logger = Logger.getLogger(VirtualMouse.class);
    private final MousePathAlgorithm algorithm = new BezierAlgorithm();
    private final Context context;
    private Component component;

    public VirtualMouse(Context context) {
        this.context = context;
    }


    public void moveMouse(final int x, final int y) {
        Mouse mouse = context.getClient().getMouse();
        Point[] path = algorithm.generatePath(
                new Point(mouse.getRealX(), mouse.getRealY()),
                new Point(x, y));
        for (Point p : path) {
            hopMouse(p.x, p.y);
            sleepNoException(random(1, 3));
        }
    }

    public void clickMouse(boolean left) {
        Mouse mouse = context.getClient().getMouse();
        clickMouse(mouse.getRealX(), mouse.getRealY(), left);
    }

    public void clickMouse(int x, int y, boolean left) {
        if (inBounds(x, y)) {
            moveMouse(x, y);
            pressMouse(x, y, left);
            releaseMouse(x, y, left);
            MouseEvent event = new MouseEvent(component, MouseEvent.MOUSE_CLICKED,
                    System.currentTimeMillis(), 0, x, y, 1, false,
                    left ? MouseEvent.BUTTON1 : MouseEvent.BUTTON3);
            context.getClient().getMouse().mouseClicked(event);
            ((MouseListener) context.getClient().getCanvas()).mouseClicked(event);
        }
    }

    private boolean inBounds(int x, int y) {
        return !(x < 0 || x > 765 || y < 0 || y > 503);
    }

    public void pressMouse(int x, int y, boolean left) {
        MouseEvent event = new MouseEvent(component, MouseEvent.MOUSE_PRESSED,
                System.currentTimeMillis(), 0, x, y, 1, false,
                left ? MouseEvent.BUTTON1 : MouseEvent.BUTTON3);
        context.getClient().getMouse().mousePressed(event);
        ((MouseListener) context.getClient().getCanvas()).mousePressed(event);
    }

    public void releaseMouse(int x, int y, boolean left) {
        MouseEvent event = new MouseEvent(component, MouseEvent.MOUSE_RELEASED,
                System.currentTimeMillis(), 0, x, y, 1, false,
                left ? MouseEvent.BUTTON1 : MouseEvent.BUTTON3);
        context.getClient().getMouse().mouseReleased(event);
        ((MouseListener) context.getClient().getCanvas()).mouseReleased(event);
    }

    public void hopMouse(int x, int y) {
        if (inBounds(x, y)) {
            MouseEvent event = new MouseEvent(component, MouseEvent.MOUSE_MOVED,
                    System.currentTimeMillis(), 0, x, y, 0, false);
            context.getClient().getMouse().mouseMoved(event);
            ((MouseMotionListener) context.getClient().getCanvas()).mouseMoved(event);
        }
    }

    public void setComponent(Component component) {
        if (this.component == null) {
            this.component = component;
        } else if (!this.component.equals(component)) {
            this.component = component;
        }
    }

    private int random(int min, int max) {
        return (int) (min + (Math.random() * max));
    }


    private void sleepNoException(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public interface MousePathAlgorithm {

        Point[] generatePath(Point origin, Point destination);

    }
}
