package ms.aurora.api.wrappers;

import com.google.common.base.Function;
import ms.aurora.api.ClientContext;
import ms.aurora.api.rt3.Widget;

import java.util.Arrays;

import static com.google.common.collect.Collections2.transform;

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

    public int getItemId() {
        return widget.getItemId();
    }

    public int getItemStackSize() {
        return widget.getItemStackSize();
    }

    public int getX() {
        return widget.getX();
    }

    public int getY() {
        return widget.getY();
    }

    public int getWidth() {
        return widget.getWidth();
    }

    public int getHeight() {
        return widget.getHeight();
    }

    public int getParentId() {
        return widget.getParentId();
    }

    public RSWidget[] getChildren() {
        return transform(Arrays.asList(widget.getChildren()), MAP_WIDGET).toArray(new RSWidget[0]);
    }

    public RSWidget getChild(int id) {
        return getChildren()[id];
    }

    private final Function<Widget, RSWidget> MAP_WIDGET = new Function<Widget, RSWidget>() {
        @Override
        public RSWidget apply(Widget component) {
            if (component != null)
                return new RSWidget(context, component);
            return null;
        }
    };
}
