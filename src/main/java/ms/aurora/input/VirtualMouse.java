package ms.aurora.input;

import ms.aurora.api.Context;
import ms.aurora.input.algorithm.BezierAlgorithm;
import ms.aurora.rt3.Mouse;
import org.apache.log4j.Logger;

import java.awt.*;
import java.awt.event.MouseEvent;

/**
 * This class controls virtual mouse movement.
 *
 * @author rvbiljouw
 * @author tobiewarburton
 */
public final class VirtualMouse {
    private static final Logger logger = Logger.getLogger(VirtualMouse.class);
    private static final MousePathAlgorithm algorithm = new BezierAlgorithm();

    public static void moveMouse(final int x, final int y) {
        Point currentPosition = new Point(getMouse().getRealX(), getMouse().getRealY());
        Point[] path = algorithm.generatePath(currentPosition, new Point(x, y));
        for (Point p : path) {
            hopMouse(p.x, p.y);
            sleepNoException(random(1, 3));
        }
    }

    public static void clickMouse(boolean left) {
        Point currentPosition = new Point(getMouse().getRealX(), getMouse().getRealY());
        clickMouse(currentPosition.x, currentPosition.y, left);
    }

    public static void clickMouse(int x, int y, boolean left) {
        moveMouse(x, y);
        pressMouse(x, y, left);
        releaseMouse(x, y, left);
        MouseEvent event = new MouseEvent(getComponent(), MouseEvent.MOUSE_CLICKED,
                System.currentTimeMillis(), 0, x, y, 1, false,
                left ? MouseEvent.BUTTON1 : MouseEvent.BUTTON3);
        getMouse().mouseClicked(event);
    }

    public static void pressMouse(int x, int y, boolean left) {
        MouseEvent event = new MouseEvent(getComponent(), MouseEvent.MOUSE_PRESSED,
                System.currentTimeMillis(), 0, x, y, 1, false,
                left ? MouseEvent.BUTTON1 : MouseEvent.BUTTON3);
        getMouse().mousePressed(event);
    }

    public static void releaseMouse(int x, int y, boolean left) {
        MouseEvent event = new MouseEvent(getComponent(), MouseEvent.MOUSE_RELEASED,
                System.currentTimeMillis(), 0, x, y, 1, false,
                left ? MouseEvent.BUTTON1 : MouseEvent.BUTTON3);
        getMouse().mouseReleased(event);
    }

    public static void hopMouse(int x, int y) {
        MouseEvent event = new MouseEvent(getComponent(), MouseEvent.MOUSE_MOVED,
                System.currentTimeMillis(), 0, x, y, 0, false);
        getMouse().mouseMoved(event);
    }

    public static Mouse getMouse() {
        return Context.get().getClient().getMouse();
    }

    public static Component getComponent() {
        return Context.get().getClient().getCanvas();
    }

    private static int random(int min, int max) {
        return (int) (min + (Math.random() * max));
    }


    private static void sleepNoException(int ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public interface MousePathAlgorithm {

        Point[] generatePath(Point origin, Point destination);

    }

    public interface DynamicTarget {
        int getX();

        int getY();

        boolean isValid();

        void onComplete();
    }
}
