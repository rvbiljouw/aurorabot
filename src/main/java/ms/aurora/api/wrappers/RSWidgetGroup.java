package ms.aurora.api.wrappers;

import ms.aurora.rt3.Widget;

/**
 * @author Rick
 */
public class RSWidgetGroup {
    private final RSWidget[] widgets;
    private final int index;

    public RSWidgetGroup(Widget[] widgets, int index) {
        this.index = index;
        if (widgets != null) {
            this.widgets = new RSWidget[widgets.length];

            for (int child = 0; child < widgets.length; child++) {
                Widget widget = widgets[child];
                if (widget != null) {
                    this.widgets[child] = new RSWidget(widget, index, child);
                }
            }
        } else {
            this.widgets = new RSWidget[0];
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
