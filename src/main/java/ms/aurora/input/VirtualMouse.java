package ms.aurora.input;

import ms.aurora.api.Context;
import ms.aurora.input.algorithm.BezierAlgorithm;
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
public final class VirtualMouse implements MouseMotionListener {
    private static final Logger logger = Logger.getLogger(VirtualMouse.class);
    private final MousePathAlgorithm algorithm = new BezierAlgorithm();
    private final Context context;
    private Component component;

    private Point mousePosition = new Point(-1, -1);

    public VirtualMouse(Context context) {
        this.context = context;
    }


    public void moveMouse(final int x, final int y) {
        Point[] path = algorithm.generatePath(mousePosition,
                new Point(x, y));
        for (Point p : path) {
            hopMouse(p.x, p.y);
            sleepNoException(random(1, 3));
        }
    }

    public void moveMouse(DynamicTarget target) {
        Point[] currentPath = new Point[0];
        int currentStep = 0, targetX = target.getX(), targetY = target.getY();

        while (mousePosition.distance(targetX, targetY) >= 3) {
            int nTargetX = target.getX();
            int nTargetY = target.getY();
            if (!target.isValid()) {
                logger.warn("Aborted, target " + target.getX() + "," +
                        target.getY() + " is no longer valid.");
                return;
            }

            if (targetX != nTargetX || targetY != nTargetY || currentStep >= currentPath.length) {
                targetX = nTargetX;
                targetY = nTargetY;
                currentPath = algorithm.generatePath(mousePosition,
                        new Point(targetX, targetY));
                logger.info("Target updated " + targetX + "," + targetY);
                currentStep = 0;
            } else {
                if (currentStep < currentPath.length) {
                    Point step = currentPath[currentStep++];
                    hopMouse(step.x, step.y);
                    logger.info("Moved stepping " + currentStep + " of " + currentPath.length);
                }
            }

            sleepNoException(random(1, 2));
        }
        target.onComplete();
    }

    public void clickMouse(boolean left) {
        clickMouse(mousePosition.x, mousePosition.y, left);
    }

    public void clickMouse(int x, int y, boolean left) {
        moveMouse(x, y);
        pressMouse(x, y, left);
        releaseMouse(x, y, left);
        MouseEvent event = new MouseEvent(component, MouseEvent.MOUSE_CLICKED,
                System.currentTimeMillis(), 0, x, y, 1, false,
                left ? MouseEvent.BUTTON1 : MouseEvent.BUTTON3);
        context.getClient().getMouse().mouseClicked(event);
        ((MouseListener) context.getClient().getCanvas()).mouseClicked(event);
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
        MouseEvent event = new MouseEvent(component, MouseEvent.MOUSE_MOVED,
                System.currentTimeMillis(), 0, x, y, 0, false);
        mouseMoved(event);
        context.getClient().getMouse().mouseMoved(event);
        ((MouseMotionListener) context.getClient().getCanvas()).mouseMoved(event);
    }

    public void setComponent(Component component) {
        if (this.component == null) {
            this.component = component;
        } else if (!this.component.equals(component)) {
            this.component = component;
            this.component.addMouseMotionListener(this);
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

    @Override
    public void mouseDragged(MouseEvent e) {
        mousePosition = e.getPoint();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        mousePosition = e.getPoint();
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
