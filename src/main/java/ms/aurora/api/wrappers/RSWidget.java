package ms.aurora.api.wrappers;

import ms.aurora.api.ClientContext;
import ms.aurora.api.util.Utilities;
import ms.aurora.input.VirtualMouse;
import ms.aurora.rt3.Mouse;
import ms.aurora.rt3.Widget;
import ms.aurora.rt3.WidgetNode;

import java.awt.*;

/**
 * @author rvbiljouw
 */
public final class RSWidget implements Interactable {
    private ClientContext ctx;
    private Widget widget;
    private int group;
    private int index;

    public RSWidget(ClientContext ctx, Widget widget, int group, int index) {
        this.ctx = ctx;
        this.widget = widget;
        this.group = group;
        this.index = index;
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
            int[] posX = ctx.getClient().getBoundsX();
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
            int[] posY = ctx.getClient().getBoundsY();
            if (getBoundsIndex() != -1 && posY[getBoundsIndex()] > 0) {
                return (posY[getBoundsIndex()] + (getType() > 0 ? widget.getY() : 0));
            }
        }
        return (widget.getY() + y);
    }

    public int getWidth() {
        int[] widthBounds = ctx.getClient().getBoundsWidth();
        int width = widget.getWidth();
        /*if (getBoundsIndex() > 0 && getBoundsIndex() < widthBounds.length) {
            width += widthBounds[getBoundsIndex()];
        }      */
        return width;
    }

    public int getHeight() {
        int[] heightBounds = ctx.getClient().getBoundsHeight();
        int height = widget.getHeight();
        /*if (getBoundsIndex() > 0 && getBoundsIndex() < heightBounds.length) {
            height += heightBounds[getBoundsIndex()];
        } */
        return height;
    }

    public String getText() {
        return widget.getText();
    }

    public RSWidget[] getChildren() {
        Widget[] children = widget.getChildren();
        if (children != null) {
            RSWidget[] wrappedChildren = new RSWidget[children.length];
            for (int i = 0; i < children.length; i++) {
                if (children[i] != null) {
                    RSWidget widget = new RSWidget(ctx, children[i], index, i);
                    wrappedChildren[i] = widget;
                } else {
                    wrappedChildren[i] = null;
                }
            }
            return wrappedChildren;
        }
        return new RSWidget[0];
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
            RSBag bag = new RSBag(ctx.getClient().getWidgetNodeBag());
            for (WidgetNode n = (WidgetNode) bag.getFirst(); n != null; n = (WidgetNode) bag.next().getNext()) {
                if (n.getId() == groupIdx) {
                    uid = n.getId();
                }
            }
        }
        if (uid == -1) {
            return null;
        }
        int parent = uid >> 16;
        int child = uid & 0xffff;
        return ctx.widgets.getWidget(parent, child);
    }

    /**
     * @return The widget's id
     */
    public int getId() {
        return (widget.getParentId() & 0xFFFF);
    }

    /**
     * @return The widget's parent id
     */
    public int getParentId() {
        return widget.getParentId() >> 16;
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
        VirtualMouse mouse = ctx.input.getMouse();
        Point randomPoint = this.getRandomPoint();
        mouse.moveMouse(randomPoint.x, randomPoint.y);
        return ctx.menu.click(action);
    }

    @Override
    public boolean hover() {
        VirtualMouse virtualMouse = ctx.input.getMouse();
        Point randomPoint = this.getRandomPoint();
        virtualMouse.moveMouse(randomPoint.x, randomPoint.y);
        Mouse clientMouse = ctx.getClient().getMouse();
        return this.getArea().contains(clientMouse.getMouseX(), clientMouse.getMouseY());
    }

    @Override
    public boolean click(boolean left) {
        VirtualMouse mouse = ctx.input.getMouse();
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

    public int getTextureId() {
        return widget.getTextureId();
    }

    public String[] getActions() {
        return widget.getActions();
    }

    @Override
    public String toString() {
        return String.valueOf(group + "," + index);
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getGroup() {
        return group;
    }

    public void setGroup(int group) {
        this.group = group;
    }
}
