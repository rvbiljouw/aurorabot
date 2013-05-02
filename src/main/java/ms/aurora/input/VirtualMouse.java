package ms.aurora.input;

import ms.aurora.api.Context;
import ms.aurora.api.util.Utilities;
import ms.aurora.input.algorithm.ZetaMouseGenerator;
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
 * @author Matty Cov
 */
public final class VirtualMouse {
    private static final Logger logger = Logger.getLogger(VirtualMouse.class);
    private static final MousePathGenerator algorithm = new ZetaMouseGenerator();

    private static void dispatchEvent(MouseEvent event) {
        if (event.getID() == MouseEvent.MOUSE_MOVED) {
            getMouse().mouseMoved(event);
        } else if (event.getID() == MouseEvent.MOUSE_CLICKED) {
            getMouse().mouseClicked(event);
        } else if (event.getID() == MouseEvent.MOUSE_PRESSED) {
            getMouse().mousePressed(event);
        } else if (event.getID() == MouseEvent.MOUSE_RELEASED) {
            getMouse().mouseReleased(event);
        } else if (event.getID() == MouseEvent.MOUSE_EXITED) {
            getMouse().mouseExited(event);
        } else if (event.getID() == MouseEvent.MOUSE_ENTERED) {
            getMouse().mouseEntered(event);
        } else if (event.getID() == MouseEvent.MOUSE_DRAGGED) {
            getMouse().mouseDragged(event);
        } else {
            System.out.println("Invalid mouse event type: " + event);
        }
    }

    public static void moveMouse(final Point target) {
        moveMouse(target.x, target.y);
    }

    public static void moveMouse(final int x, final int y) {
        Point currentPosition = null;
        while ((currentPosition = new Point(getMouse().getRealX(), getMouse().getRealY())).distance(new Point(x,y)) > 4 && !Thread.currentThread().isInterrupted()) {
            Point[] path = algorithm.generate(currentPosition, new Point(x, y));
            MouseEventChain chain = MouseEventChain.createMousePath(path);
            MouseEvent[] events = chain.getMouseEvents();
            int[] sleepTimes = chain.getSleepTimes();
            for (int i = 0; i < events.length; i++) {
                dispatchEvent(events[i]);
                Utilities.sleepNoException(sleepTimes[i]);
            }
        }
    }

    public static void clickMouse(boolean left) {
        clickMouse(getMouse().getRealX(), getMouse().getRealY(), left);
    }

    public static void clickMouse(int x, int y, boolean left) {
        moveMouse(x, y);
        MouseEventChain chain = MouseEventChain.createMouseClick(new Point(x, y), left ? MouseEvent.BUTTON1 : MouseEvent.BUTTON3, 1);
        MouseEvent[] events = chain.getMouseEvents();
        int[] sleepTimes = chain.getSleepTimes();
        for (int i = 0; i < events.length; i++) {
            dispatchEvent(events[i]);
            Utilities.sleepNoException(sleepTimes[i]);
        }
    }

    public static void dragMouse(Point target, boolean left) {
        dragMouse(target.x, target.y, left);
    }

    public static void dragMouse(int x, int y, boolean left) {
        Point currentPosition = new Point(getMouse().getRealX(), getMouse().getRealY());
        Point[] path = algorithm.generate(currentPosition, new Point(x, y));
        MouseEventChain chain = MouseEventChain.createMouseDrag(path, left ? MouseEvent.BUTTON1 : MouseEvent.BUTTON3);
        MouseEvent[] events = chain.getMouseEvents();
        int[] sleepTimes = chain.getSleepTimes();
        for (int i = 0; i < events.length; i++) {
            dispatchEvent(events[i]);
            Utilities.sleepNoException(sleepTimes[i]);
        }
    }

    @Deprecated
    public static void pressMouse(int x, int y, boolean left) {
        MouseEvent event = new MouseEvent(getComponent(), MouseEvent.MOUSE_PRESSED,
                System.currentTimeMillis(), 0, x, y, 1, false,
                left ? MouseEvent.BUTTON1 : MouseEvent.BUTTON3);
        getMouse().mousePressed(event);
    }

    @Deprecated
    public static void releaseMouse(int x, int y, boolean left) {
        MouseEvent event = new MouseEvent(getComponent(), MouseEvent.MOUSE_RELEASED,
                System.currentTimeMillis(), 0, x, y, 1, false,
                left ? MouseEvent.BUTTON1 : MouseEvent.BUTTON3);
        getMouse().mouseReleased(event);
    }

    @Deprecated
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

    @Deprecated
    private static double getSpeed(int percentage) {
        double a = random.nextDouble() * 0.01 + 0.0001;
        double b = random.nextDouble() * 0.03 + 0.005;
        double speedFactor = a * Math.pow(percentage, 2) + b * percentage;
        return Utilities.random(0.05, 1.0) + speedFactor;
    }

    private static double speed = 7;

    private static final Random random = new Random();

}
