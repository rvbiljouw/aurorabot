package ms.aurora.api.wrappers;

import com.google.common.base.Function;
import ms.aurora.api.*;
import ms.aurora.api.Menu;
import ms.aurora.api.rt3.Mouse;
import ms.aurora.api.rt3.Widget;
import ms.aurora.api.rt3.WidgetNode;
import ms.aurora.api.util.Utilities;
import ms.aurora.input.VirtualMouse;

import java.awt.*;
import java.util.Arrays;

import static com.google.common.collect.Collections2.transform;

/**
 * @author rvbiljouw
 */
public class RSWidget implements Interactable {
    private ClientContext context;
    private Widget widget;

    public RSWidget(ClientContext context, Widget widget) {
        this.context = context;
        this.widget = widget;
    }

    public int getUid() {
        return widget.getUid();
    }

    public int getItemId() {
        return widget.getItemId();
    }

    public int getItemStackSize() {
        return widget.getItemStackSize();
    }


    public int getBoundsIndex() {
        return widget.getBoundsIndex();
    }

    public int getType() {
        return widget.getType();
    }

    public int getX() {
        if (widget == null) {
            return -1;
        }
        RSWidget parent = getParent();
        int x = 0;
        if (parent != null) {
            x = parent.getX();
        } else {
            int[] posX = context.getClient().getBoundsX();
            if (getBoundsIndex() != -1 && posX[getBoundsIndex()] > 0) {
                return (posX[getBoundsIndex()] + (getType() > 0 ? widget.getX() : 0));
            }
        }
        return (widget.getX() + x);
    }

    public int getY() {
        if (widget == null) {
            return -1;
        }
        RSWidget parent = getParent();
        int y = 0;
        if (parent != null) {
            y = parent.getY();
        } else {
            int[] posY = context.getClient().getBoundsY();
            if (getBoundsIndex() != -1 && posY[getBoundsIndex()] > 0) {
                return (posY[getBoundsIndex()] + (getType() > 0 ? widget.getY() : 0));
            }
        }
        return (widget.getY() + y);
    }

    public int getWidth() {
        int[] widthBounds = context.getClient().getBoundsWidth();
        int width = widget.getWidth();
        if (getBoundsIndex() > 0 && getBoundsIndex() < widthBounds.length) {
            width += widthBounds[getBoundsIndex()];
        }
        return width;
    }

    public int getHeight() {
        int[] heightBounds = context.getClient().getBoundsHeight();
        int height = widget.getHeight();
        if (getBoundsIndex() > 0 && getBoundsIndex() < heightBounds.length) {
            height += heightBounds[getBoundsIndex()];
        }
        return height;
    }

    public int getParentId() {
        return widget.getParentId();
    }

    public String getText() {
        return widget.getText();
    }

    public RSWidget[] getChildren() {
        return transform(Arrays.asList(widget.getChildren()), MAP_WIDGET).toArray(new RSWidget[0]);
    }

    public RSWidget getChild(int id) {
        return getChildren()[id];
    }

    public RSWidget getParent() {
        if (widget == null) {
            return null;
        }
        int uid = getParentId();
        if (uid == -1) {
            int groupIdx = getUid() >>> 16;
            RSBag bag = new RSBag(context.getClient().getWidgetNodeBag());
            for (WidgetNode n = (WidgetNode) bag.getFirst(); n != null; n = (WidgetNode) bag.next().getNext()) {
                if (n.getId() == groupIdx) {
                    uid = (int) n.getId();
                }
            }
        }
        if (uid == -1) {
            return null;
        }
        int parent = uid >> 16;
        int child = uid & 0xffff;
        return Widgets.getWidget(parent, child);
    }

    /**
     * @return a rectangle representation of the widget area
     */
    public Rectangle getArea() {
        return new Rectangle(getX(), getY(), getWidth(), getHeight());
    }

    /**
     * returns the central point inside the widget bounds
     *
     * @return the central point inside the widget bounds.
     */
    public Point getCenterPoint() {
        Rectangle area = getArea();
        return new Point((int) area.getCenterX(), (int) area.getCenterY());
    }

    /**
     * returns a random point inside the widget bounds
     *
     * @return the random point inside the widget
     */
    public Point getRandomPoint() {
        Rectangle area = getArea();
        int x = Utilities.random(area.x, area.width);
        int y = Utilities.random(area.y, area.height);
        return new Point(x, y);
    }

    @Override
    public boolean applyAction(String action) {
        VirtualMouse mouse = ClientContext.get().input.getMouse();
        Point randomPoint = this.getRandomPoint();
        mouse.moveMouse(randomPoint.x, randomPoint.y);
        Menu.click(action);
        return true;
    }

    @Override
    public boolean hover() {
        VirtualMouse virtualMouse = ClientContext.get().input.getMouse();
        Point randomPoint = this.getRandomPoint();
        virtualMouse.moveMouse(randomPoint.x, randomPoint.y);
        Mouse clientMouse = ClientContext.get().getClient().getMouse();
        return this.getArea().contains(clientMouse.getMouseX(), clientMouse.getMouseY());
    }

    @Override
    public boolean click(boolean left) {
        VirtualMouse mouse = ClientContext.get().input.getMouse();
        Point randomPoint = this.getRandomPoint();
        mouse.clickMouse(randomPoint.x, randomPoint.y, left);
        return true;
    }

    public int[] getInventoryItems() {
        return widget.getInventoryItems();
    }

    public int[] getInventoryStackSizes() {
        return widget.getInventoryStackSizes();
    }

    private final Function<Widget, RSWidget> MAP_WIDGET = new Function<Widget, RSWidget>() {
        @Override
        public RSWidget apply(Widget component) {
            if (component != null) {
                return new RSWidget(context, component);
            }
            return null;
        }
    };
}
