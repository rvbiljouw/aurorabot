package ms.aurora.api.wrappers;

import ms.aurora.api.Context;
import ms.aurora.api.util.Utilities;
import ms.aurora.input.VirtualMouse;
import ms.aurora.rt3.Mouse;

import java.awt.*;

/**
 * Date: 29/04/13
 * Time: 11:47
 *
 * @author A_C/Cov
 */
public class RSWidgetItem implements Interactable {

    private Rectangle area;
    private int id, stackSize;

    public RSWidgetItem(Rectangle area, int id, int stackSize) {
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
        int x = Utilities.random(area.x, area.x + area.width);
        int y = Utilities.random(area.y, area.y + area.height);
        return new Point(x, y);
    }

    @Override
    public boolean applyAction(String action) {
        Point p = getRandomPoint();
        VirtualMouse.moveMouse(p.x, p.y);
        return ms.aurora.api.methods.Menu.click(action);
    }

    @Override
    public boolean hover() {
        Point p = getRandomPoint();
        VirtualMouse.moveMouse(p.x, p.y);
        Mouse clientMouse = Context.getClient().getMouse();
        return area.contains(clientMouse.getRealX(), clientMouse.getRealX());
    }

    @Override
    public boolean click(boolean left) {
        Point p = getRandomPoint();
        VirtualMouse.clickMouse(p.x, p.y, left);
        return true;
    }
}
