package ms.aurora.input;

import ms.aurora.api.Context;
import ms.aurora.api.util.Utilities;
import ms.aurora.input.algorithm.StraightLineGenerator;
import ms.aurora.input.algorithm.ZetaMouseGenerator;
import ms.aurora.rt3.IMouse;

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
        }
    }

    public static void moveMouse(final Point target) {
        moveMouse(target.x, target.y);
    }

    public static void moveMouse(final int x, final int y) {
        Point currentPosition;
        Point target = new Point(x, y);
        while ((currentPosition = new Point(getMouse().getRealX(), getMouse().getRealY())).distance(target) > 4 && !Thread.currentThread().isInterrupted()) {
            MousePathGenerator algorithm = new ZetaMouseGenerator();
            if (target.distance(currentPosition) < 20) {
                algorithm = new StraightLineGenerator();
            }
            Point[] path = algorithm.generate(currentPosition, new Point(x, y));
            MouseEventChain chain = MouseEventChain.createMousePath(path);
            MouseEvent[] events = chain.getMouseEvents();
            int[] sleepTimes = chain.getSleepTimes();
            for (int i = 0; i < events.length && !Thread.currentThread().isInterrupted(); i++) {
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
        for (int i = 0; i < events.length && !Thread.currentThread().isInterrupted(); i++) {
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
        for (int i = 0; i < events.length && !Thread.currentThread().isInterrupted(); i++) {
            dispatchEvent(events[i]);
            Utilities.sleepNoException(sleepTimes[i]);
        }
    }

    public static void moveRandomly(int distance) {
        int x = getMouse().getRealX() + Utilities.random(-distance, distance);
        int y = getMouse().getRealY() + Utilities.random(-distance, distance);
        moveMouse(x, y);
    }

    public static IMouse getMouse() {
        return Context.getClient().getMouse();
    }

    public static Component getComponent() {
        return Context.getClient().getCanvas();
    }

    private static final Random random = new Random();

}
