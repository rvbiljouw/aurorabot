package ms.aurora.core.input;

import java.awt.*;

/**
 * @author rvbiljouw
 */
public interface MouseProvider {

    /**
     * Moves mouse to a particular point x and y on the applet.
     *
     * @param x
     * @param y
     */
    public void moveMouse(int x, int y);

    /**
     * Moves the mouse to x/y if we're not there yet, then clicks the mouse at that point.
     *
     * @param x
     * @param y
     * @param left Do we want to do a left click?
     */
    public void clickMouse(int x, int y, boolean left);

    /**
     * Set the target component for all further events.
     *
     * @param component
     */
    public void setComponent(Component component);
}
