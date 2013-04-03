package ms.aurora.api.wrappers;

import ms.aurora.api.Context;
import ms.aurora.rt3.Widget;

/**
 * @author Rick
 */
public class RSWidgetGroup {
    private final Context ctx;
    private final RSWidget[] widgets;
    private final int index;

    public RSWidgetGroup(Context ctx, Widget[] widgets, int index) {
        this.widgets = new RSWidget[widgets.length];
        this.index = index;
        this.ctx = ctx;

        for (int child = 0; child < widgets.length; child++) {
            Widget widget = widgets[child];
            if (widget != null) {
                this.widgets[child] = new RSWidget(ctx, widget, index, child);
            }
        }
    }

    public boolean isValid() {
        return widgets.length > 0 && widgets[0] != null;
    }

    public RSWidget[] getWidgets() {
        return widgets;
    }

    public int getIndex() {
        return index;
    }
}
