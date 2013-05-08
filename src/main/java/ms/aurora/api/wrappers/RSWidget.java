package ms.aurora.api.wrappers;

import ms.aurora.api.Context;
import ms.aurora.api.methods.Widgets;
import ms.aurora.api.util.Utilities;
import ms.aurora.input.VirtualMouse;
import ms.aurora.rt3.Mouse;
import ms.aurora.rt3.Widget;
import ms.aurora.rt3.WidgetNode;

import java.awt.*;

import static ms.aurora.api.Context.getClient;

/**
 * @author Rick
 */
public final class RSWidget implements Interactable {
    private Context ctx;
    private Widget widget;
    private int group;
    private int index;

    public RSWidget(Context ctx, Widget widget, int group, int index) {
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
            int[] posX = getClient().getBoundsX();
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
            int[] posY = getClient().getBoundsY();
            if (getBoundsIndex() != -1 && posY[getBoundsIndex()] > 0) {
                return (posY[getBoundsIndex()] + (getType() > 0 ? widget.getY() : 0));
            }
        }
        return (widget.getY() + y);
    }

    public int getWidth() {
        return widget.getWidth();
    }

    public int getHeight() {
        return widget.getHeight();
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
        int parent = getParentId();
        if (parent != -1) {
            return Widgets.getWidget(parent >> 16, parent & 0xFFFF);
        }
        return null;
    }


    public int getParentId() {
        if (widget.getParentId() != -1) return  widget.getParentId();

        int i = getUid() >>> 16;
        RSBag<WidgetNode> cache = new RSBag<WidgetNode>(getClient().getWidgetNodeBag());
        for (WidgetNode node = cache.getFirst(); node != null; node = cache.getNext()) {
            if (i == node.getId()) {
                return (int) node.getHash();
            }
        }
        return -1;
    }

    /**
     * @return The widget's id
     */
    public int getId() {
        return index;
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
        int x = (int) (area.getCenterX() + Utilities.random(-(area.width / 2.5), area.width / 2.5));
        int y = (int) (area.getCenterY() + Utilities.random(-(area.height / 2.5), area.height / 2.5));
        return new Point(x, y);
    }

    @Override
    public boolean applyAction(String action) {
        Point randomPoint = this.getRandomPoint();
        VirtualMouse.moveMouse(randomPoint.x, randomPoint.y);
        return ms.aurora.api.methods.Menu.click(action);
    }

    @Override
    public boolean hover() {
        Point randomPoint = this.getRandomPoint();
        VirtualMouse.moveMouse(randomPoint.x, randomPoint.y);
        Mouse clientMouse = getClient().getMouse();
        return this.getArea().contains(clientMouse.getRealX(), clientMouse.getRealY());
    }

    @Override
    public boolean click(boolean left) {
        Point randomPoint = this.getRandomPoint();
        VirtualMouse.clickMouse(randomPoint.x, randomPoint.y, left);
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

    public int getModelId() {
        return widget.getModelId();
    }

    //TODO: Extra hook!
    public int getBackgroundColor() {
        return -1;
    }
}