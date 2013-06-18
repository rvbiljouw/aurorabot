package ms.aurora.api.wrappers;

import ms.aurora.api.Context;
import ms.aurora.api.util.Utilities;
import ms.aurora.input.VirtualMouse;
import ms.aurora.rt3.IMouse;

import java.awt.*;

import static ms.aurora.api.methods.Menu.contains;
import static ms.aurora.api.methods.Menu.containsPred;
import static ms.aurora.api.util.Utilities.random;

/**
 * Date: 29/04/13
 * Time: 11:47
 *
 * @author A_C/Cov
 */
public class WidgetItem implements Interactable {

    private Rectangle area;
    private int id, stackSize;

    public WidgetItem(Rectangle area, int id, int stackSize) {
        this.area = area;
        this.id = id;
        this.stackSize = stackSize;
    }

    public Rectangle getArea() {
        return area;
    }

    public int getId() {
        return id;
    }

    public int getStackSize() {
        return stackSize;
    }

    private Point getRandomPoint() {
        int hx = (area.width / 2);
        int hy = (area.height / 2);
        int x = (int) area.getCenterX() + random(-hx, hx);
        int y = (int) area.getCenterY() + random(-hy, hy);
        Point p = new Point(x, y);
        return p;
    }

    @Override
    public boolean applyAction(String actionName) {
        Point click = getRandomPoint();
        VirtualMouse.moveMouse(click.x, click.y);
        Utilities.sleepUntil(containsPred(actionName), 600);
        return contains(actionName) && ms.aurora.api.methods.Menu.click(actionName);
    }

    @Override
    public boolean hover() {
        Point p = getRandomPoint();
        VirtualMouse.moveMouse(p.x, p.y);
        IMouse clientMouse = Context.getClient().getMouse();
        return area.contains(clientMouse.getRealX(), clientMouse.getRealX());
    }

    @Override
    public boolean click(boolean left) {
        Point p = getRandomPoint();
        VirtualMouse.clickMouse(p.x, p.y, left);
        return true;
    }
}
