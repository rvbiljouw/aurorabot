package ms.aurora.api.wrappers;

import ms.aurora.api.methods.Widgets;
import ms.aurora.api.util.Utilities;
import ms.aurora.input.VirtualMouse;
import ms.aurora.input.action.impl.MouseMovedAction;
import ms.aurora.rt3.IMouse;
import ms.aurora.rt3.IWidget;
import ms.aurora.rt3.IWidgetNode;

import java.awt.*;

import static ms.aurora.api.Context.getClient;
import static ms.aurora.api.methods.Menu.containsPred;

/**
 * @author Rick
 */
public final class Widget implements Interactable {
    private IWidget widget;
    private int group;
    private int index;

    public Widget(IWidget widget, int group, int index) {
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
        Widget parent = getParent();
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
        Widget parent = getParent();
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

    public Widget[] getChildren() {
        IWidget[] children = widget.getChildren();
        if (children != null) {
            Widget[] wrappedChildren = new Widget[children.length];
            for (int i = 0; i < children.length; i++) {
                if (children[i] != null) {
                    Widget widget = new Widget(children[i], index, i);
                    wrappedChildren[i] = widget;
                } else {
                    wrappedChildren[i] = null;
                }
            }
            return wrappedChildren;
        }
        return new Widget[0];
    }

    public Widget getChild(int id) {
        return getChildren()[id];
    }


    public Widget getParent() {
        int parent = getParentId();
        if (parent != -1) {
            return Widgets.getWidget(parent >> 16, parent & 0xFFFF);
        }
        return null;
    }


    public int getParentId() {
        if (widget.getParentId() != -1) return  widget.getParentId();

        int i = getUid() >>> 16;
        Bag<IWidgetNode> cache = new Bag<IWidgetNode>(getClient().getWidgetNodeBag());
        for (IWidgetNode node = cache.getFirst(); node != null; node = cache.getNext()) {
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
    @Override
    public Point getClickLocation() {
        Rectangle area = getArea();
        int x = (int) (area.getCenterX() + Utilities.random(-(area.width / 2.5), area.width / 2.5));
        int y = (int) (area.getCenterY() + Utilities.random(-(area.height / 2.5), area.height / 2.5));
        return new Point(x, y);
    }

    @Override
    public boolean applyAction(String action) {
        /*Point randomPoint = this.getRandomPoint();
        VirtualMouse.moveMouse(randomPoint.x, randomPoint.y);*/
        new MouseMovedAction() {

            @Override
            public Point getTarget() {
                return Widget.this.getClickLocation();
            }

            @Override
            public boolean canStep() {
                return true;
            }
        }.apply();
        Utilities.sleepUntil(containsPred(action), 400);
        return ms.aurora.api.methods.Menu.contains(action) && ms.aurora.api.methods.Menu.click(action);
    }

    @Override
    public boolean hover() {
        new MouseMovedAction() {

            @Override
            public Point getTarget() {
                return Widget.this.getClickLocation();
            }

            @Override
            public boolean canStep() {
                return true;
            }
        }.apply();
        IMouse clientMouse = getClient().getMouse();
        return this.getArea().contains(clientMouse.getRealX(), clientMouse.getRealY());
    }

    @Override
    public boolean click(final boolean left) {
        new MouseMovedAction() {

            @Override
            public Point getTarget() {
                return Widget.this.getClickLocation();
            }

            @Override
            public boolean canStep() {
                return true;
            }

            @Override
            public void end() {
                VirtualMouse.clickMouse(left);
            }
        }.apply();
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