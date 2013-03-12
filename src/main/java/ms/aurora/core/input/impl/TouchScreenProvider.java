package ms.aurora.core.input.impl;

import ms.aurora.core.input.MouseProvider;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.util.Random;

/**
 * this class will act like a touch screen, moving the mouse directly to the
 * points specified.
 *
 * @author tobiewarburton
 */
public class TouchScreenProvider implements MouseProvider, MouseMotionListener {
    private Random random = new Random();
    private Component component;
    private int mouseX;
    private int mouseY;

    /**
     * this is the amount of pixels the mouse will be allowed to deviate from
     * the actual destination, the real figure of the maximum deviation is
     * randomness * 2.
     */
    private int randomness = 7;

    @Override
    public void moveMouse(int x, int y) {
        Point point = applyRandomness(x, y);
        MouseEvent event = new MouseEvent(component, MouseEvent.MOUSE_MOVED,
                System.currentTimeMillis(), 0, point.x, point.y, 0, false);
        component.dispatchEvent(event);
    }

    @Override
    public void clickMouse(boolean left) {
        clickMouse(mouseX, mouseY, left);
    }

    @Override
    public void clickMouse(int x, int y, boolean left) {
        moveMouse(x, y);
        pressMouse(x, y, left);
        releaseMouse(x, y, left);
        MouseEvent event = new MouseEvent(component, MouseEvent.MOUSE_CLICKED,
                System.currentTimeMillis(), 0, x, y, 1, false,
                left ? MouseEvent.BUTTON1 : MouseEvent.BUTTON3);
        component.dispatchEvent(event);
    }

    public void pressMouse(int x, int y, boolean left) {
        MouseEvent event = new MouseEvent(component, MouseEvent.MOUSE_PRESSED,
                System.currentTimeMillis(), 0, x, y, 1, false,
                left ? MouseEvent.BUTTON1 : MouseEvent.BUTTON3);
        component.dispatchEvent(event);
    }

    public void releaseMouse(int x, int y, boolean left) {
        MouseEvent event = new MouseEvent(component, MouseEvent.MOUSE_RELEASED,
                System.currentTimeMillis(), 0, x, y, 1, false,
                left ? MouseEvent.BUTTON1 : MouseEvent.BUTTON3);
        component.dispatchEvent(event);
    }

    @Override
    public void setComponent(Component component) {
        if (this.component == null) {
            this.component = component;
        } else if (!this.component.equals(component)) {
            this.component = component;
            this.component.addMouseMotionListener(this);
        }
    }

    private Point applyRandomness(int x, int y) {
        x += random(-randomness, randomness);
        y += random(-randomness, randomness);
        return new Point(x, y);
    }

    private int random(int min, int max) {
        return (max - min) * random.nextInt() + min;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        this.mouseX = e.getX();
        this.mouseY = e.getY();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        this.mouseX = e.getX();
        this.mouseY = e.getY();
    }
}
