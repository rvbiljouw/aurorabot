package ms.aurora.input;

import ms.aurora.api.Context;
import ms.aurora.api.util.Utilities;
import ms.aurora.input.algorithm.BezierAlgorithm;
import ms.aurora.rt3.Mouse;
import org.apache.log4j.Logger;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.Random;

/**
 * This class controls virtual mouse movement.
 *
 * @author rvbiljouw
 * @author tobiewarburton
 */
public final class VirtualMouse {
    private static final Logger logger = Logger.getLogger(VirtualMouse.class);
    private static final MousePathAlgorithm algorithm = new BezierAlgorithm();

    public static void moveMouse(final Point target) {
        moveMouse(target.x, target.y);
    }

    public static void moveMouse(final int x, final int y) {
        Point currentPosition = new Point(getMouse().getRealX(), getMouse().getRealY());
        Point[] path = algorithm.generatePath(currentPosition, new Point(x, y));
        for (int i = 0; i < path.length; i++) {
            Point p = path[i];
            speed = getSpeed((i / path.length) * 100);
            hopMouse(p.x, p.y);
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
        int secs = (int) speed;
        double nanos = speed - secs;
        int nanosReal = (int) (nanos * 1000);
        try {
            Thread.sleep(secs, nanosReal);
        } catch (InterruptedException ignored) {
        }
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

    private static double random(double min, double max) {
        return (min + (Math.random() * max));
    }

    private static double getSpeed(int percentage) {
        double a = random.nextDouble() * 0.01 + 0.0001;
        double b = random.nextDouble() * 0.03 + 0.005;
        double speedFactor = a * Math.pow(percentage, 2) + b * percentage;
        return random(0.05, 1.0) + speedFactor;
    }

    private static double speed = 7;

    public interface MousePathAlgorithm {

        Point[] generatePath(Point origin, Point destination);

    }

    private static final Random random = new Random();

}
