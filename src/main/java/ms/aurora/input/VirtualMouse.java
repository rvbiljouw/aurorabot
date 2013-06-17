package ms.aurora.input;

import ms.aurora.api.Context;
import ms.aurora.api.util.Utilities;
import ms.aurora.input.action.MouseInputAction;
import ms.aurora.input.action.impl.MouseDraggedAction;
import ms.aurora.input.action.impl.MouseMovedAction;
import ms.aurora.rt3.Mouse;

import java.awt.*;
import java.awt.event.MouseEvent;

/**
 * This class controls virtual mouse movement.
 *
 * @author rvbiljouw
 * @author tobiewarburton
 * @author Matty Cov
 */
public final class VirtualMouse {

    public static void dispatchEvent(MouseEvent event) {
        switch (event.getID()) {
            case MouseEvent.MOUSE_MOVED:
                getMouse().mouseMoved(event);
                break;
            case MouseEvent.MOUSE_CLICKED:
                getMouse().mouseClicked(event);
                break;
            case MouseEvent.MOUSE_PRESSED:
                getMouse().mousePressed(event);
                break;
            case MouseEvent.MOUSE_RELEASED:
                getMouse().mouseReleased(event);
                break;
            case MouseEvent.MOUSE_EXITED:
                getMouse().mouseExited(event);
                break;
            case MouseEvent.MOUSE_ENTERED:
                getMouse().mouseEntered(event);
                break;
            case MouseEvent.MOUSE_DRAGGED:
                getMouse().mouseDragged(event);
                break;
        }
    }

    public static void dispatchChain(MouseEventChain chain) {
        MouseEvent[] events = chain.getMouseEvents();
        int[] sleepTimes = chain.getSleepTimes();
        for (int i = 0; i < events.length && !Thread.currentThread().isInterrupted(); i++) {
            dispatchEvent(events[i]);
            Utilities.sleepNoException(sleepTimes[i]);
        }
    }

    public static void moveMouse(final Point target) {
        moveMouse(target.x, target.y);
    }

    public static void moveMouse(final int x, final int y) {
        /*Point currentPosition;
        Point target = new Point(x, y);
        while ((currentPosition = new Point(getMouse().getRealX(), getMouse().getRealY())).distance(target) > 4 && !Thread.currentThread().isInterrupted()) {
            Point[] path = target.distance(currentPosition) < 20 ? STRAIGHT_MOUSE_GENERATOR.generate(currentPosition, target) :
                    ZETA_MOUSE_GENERATOR.generate(currentPosition, target);
            MouseEventChain chain = MouseEventChain.createMousePath(path);
            dispatchChain(chain);
        }*/
        new MouseMovedAction() {
            @Override
            public Point getTarget() {
                return new Point(x, y);
            }

            @Override
            public boolean canStep() {
                return true;
            }
        }.apply();
    }

    public static void clickMouse(boolean left) {
        clickMouse(getMouse().getRealX(), getMouse().getRealY(), left);
    }

    public static void clickMouse(int x, int y, boolean left) {
        moveMouse(x, y);
        MouseEventChain chain = MouseEventChain.createMouseClick(new Point(x, y), left ? MouseEvent.BUTTON1 : MouseEvent.BUTTON3, 1);
        dispatchChain(chain);
    }

    public static void dragMouse(Point target) {
        dragMouse(target.x, target.y);
    }

    public static void dragMouse(final int x, final int y) {
        /*Point currentPosition = new Point(getMouse().getRealX(), getMouse().getRealY());
        Point[] path = ZETA_MOUSE_GENERATOR.generate(currentPosition, new Point(x, y));
        MouseEventChain chain = MouseEventChain.createMouseDrag(path, left ? MouseEvent.BUTTON1 : MouseEvent.BUTTON3);
        dispatchChain(chain);*/
        new MouseDraggedAction() {

            @Override
            public Point getTarget() {
                return new Point(x, y);
            }

            @Override
            public boolean canStep() {
                return true;
            }
        }.apply();
    }

    public static void moveRandomly(int distance) {
        int x = getMouse().getRealX() + Utilities.random(-distance, distance);
        int y = getMouse().getRealY() + Utilities.random(-distance, distance);
        moveMouse(x, y);
    }

    public static Mouse getMouse() {
        return Context.getClient().getMouse();
    }

}
