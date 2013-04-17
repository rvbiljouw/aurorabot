package ms.aurora.input;

import ms.aurora.api.Context;
import ms.aurora.api.util.Utilities;
import ms.aurora.input.algorithm.BezierAlgorithm;
import ms.aurora.input.algorithm.StraightLineAlgorithm;
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
    private static final MousePathAlgorithm alternate = new StraightLineAlgorithm();
    private static final MousePathAlgorithm algorithm = new BezierAlgorithm();

    public static void moveMouse(final Point target) {
        moveMouse(target.x, target.y);
    }

    public static void moveMouse(final int x, final int y) {
        Point currentPosition = new Point(getMouse().getRealX(), getMouse().getRealY());
        Point[] path = algorithm.generatePath(currentPosition, new Point(x, y));
        for (Point p : path) {
            hopMouse(p.x, p.y);
            sleepNoExceptionNano(random(1000000, 500000));
        }
    }

    public static void clickMouse(boolean left) {
        Point currentPosition = new Point(getMouse().getRealX(), getMouse().getRealY());
        clickMouse(currentPosition.x, currentPosition.y, left);
    }

    public static void clickMouse(int x, int y, boolean left) {
        moveMouse(x, y);
        sleepNoException(random(150, 250));
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

    public static void moveRandomly(int distance) {
        int x = getMouse().getRealX() + Utilities.random(-distance, distance);
        int y = getMouse().getRealY() + Utilities.random(-distance, distance);
        moveMouse(x, y);
    }

    public static Mouse getMouse() {
        return Context.getClient().getMouse();
    }

    public static Component getComponent() {
        return Context.getClient().getCanvas();
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

    private static void sleepNoExceptionNano(long ns) {
        long curNs = System.nanoTime();
        while ((System.nanoTime() - curNs) < ns && !Thread.interrupted()) {

        }
    }

    public interface MousePathAlgorithm {

        Point[] generatePath(Point origin, Point destination);

    }

    public interface DynamicTarget {

        Point getTarget();

        boolean isValid();

        Point getReference();
    }
}
