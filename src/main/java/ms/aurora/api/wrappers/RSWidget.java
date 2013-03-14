package ms.aurora.api.wrappers;

import ms.aurora.api.ClientContext;
import ms.aurora.api.Widgets;
import ms.aurora.api.rt3.Widget;
import ms.aurora.api.rt3.WidgetNode;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

/**
 * @author rvbiljouw
 */
public class RSWidget {
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
                    uid = (int) n.getHash();
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

    public RSWidget[] getChildren() {
        List<RSWidget> children = newArrayList();
        if (widget.getChildren() != null) {
            for (Widget child : widget.getChildren()) {
                if (child != null)
                    children.add(new RSWidget(context, child));
            }
        }
        return children.toArray(new RSWidget[children.size()]);
    }

    public RSWidget getChild(int id) {
        return getChildren()[id];
    }
}
