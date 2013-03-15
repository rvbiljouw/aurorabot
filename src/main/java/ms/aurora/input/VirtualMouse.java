package ms.aurora.input;

import ms.aurora.input.algorithm.BezierAlgorithm;
import org.apache.log4j.Logger;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

/**
 * this class will act like mouse
 *
 * @author rvbiljouw
 * @author tobiewarburton
 */
public class VirtualMouse implements MouseMotionListener, Runnable {
    private static final Logger logger = Logger.getLogger(VirtualMouse.class);
    private final MousePathAlgorithm algorithm = new BezierAlgorithm();
    private Component component;
    private Point mousePosition = new Point(-1, -1);

    private Point[] currentPath = new Point[0];
    private int currentStep = 0;
    private boolean moving = false;

    private int targetX = 0;
    private int targetY = 0;


    public void moveMouse(int x, int y) {
        targetX = x;
        targetY = y;
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
        postEvent(event);
    }

    public void pressMouse(int x, int y, boolean left) {
        MouseEvent event = new MouseEvent(component, MouseEvent.MOUSE_PRESSED,
                System.currentTimeMillis(), 0, x, y, 1, false,
                left ? MouseEvent.BUTTON1 : MouseEvent.BUTTON3);
        postEvent(event);
    }

    public void releaseMouse(int x, int y, boolean left) {
        MouseEvent event = new MouseEvent(component, MouseEvent.MOUSE_RELEASED,
                System.currentTimeMillis(), 0, x, y, 1, false,
                left ? MouseEvent.BUTTON1 : MouseEvent.BUTTON3);
        postEvent(event);
    }

    public void hopMouse(int x, int y) {
        MouseEvent event = new MouseEvent(component, MouseEvent.MOUSE_MOVED,
                System.currentTimeMillis(), 0, x, y, 0, false);
        postEvent(event);
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
        }
    }

    public void postEvent(MouseEvent event) {
        if (event.getID() == MouseEvent.MOUSE_MOVED) {
            mousePosition = event.getPoint();
            _mouseMoved(event);
        } else if (event.getID() == MouseEvent.MOUSE_CLICKED) {
            mousePosition = event.getPoint();
            _mouseClicked(event);
        } else if (event.getID() == MouseEvent.MOUSE_PRESSED) {
            mousePosition = event.getPoint();
            // TODO: ensureFocus
            _mousePressed(event);
        } else if (event.getID() == MouseEvent.MOUSE_RELEASED) {
            mousePosition = event.getPoint();
            _mouseReleased(event);
        }
    }

    private void _mouseMoved(MouseEvent event) {
        for (MouseMotionListener listener : component.getMouseMotionListeners()) {
            listener.mouseMoved(event);
        }
    }

    private void _mouseClicked(MouseEvent event) {
        for (MouseListener listener : component.getMouseListeners()) {
            listener.mouseClicked(event);
        }
    }

    private void _mousePressed(MouseEvent event) {
        for (MouseListener listener : component.getMouseListeners()) {
            listener.mousePressed(event);
        }
    }

    private void _mouseReleased(MouseEvent event) {
        for (MouseListener listener : component.getMouseListeners()) {
            listener.mouseReleased(event);
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

    @Override
    public void run() {

        int lastTargetX = 0;
        int lastTargetY = 0;
        while (!Thread.interrupted()) {
            try {
                if (targetX != lastTargetX || targetY != lastTargetY) {
                    currentPath = algorithm.generatePath(mousePosition,
                            new Point(targetX, targetY));
                    currentStep = 0;
                    moving = true;
                    logger.info("Received a movement request.");
                }

                if (currentStep < currentPath.length && moving) {
                    Point next = currentPath[currentStep];
                    hopMouse(next.x, next.y);
                    if (mousePosition.distance(targetX, targetY) <= 3) { // If we're less than three pixels removed from target just call it quits.
                        moving = false;
                        currentStep = 0;
                        currentPath = new Point[0];
                    }
                }
                lastTargetX = targetX;
                lastTargetY = targetY;

                if(!moving) {
                    Thread.sleep(random(60, 120)); // time before realizing something has changed.
                } else {
                    Thread.sleep(random(2, 10));
                }
            } catch (InterruptedException e) {
                logger.error("Mouse thread was interrupted", e);
            }
        }
    }

    public static interface MousePathAlgorithm {
        public Point[] generatePath(Point origin, Point destination);
    }
}
