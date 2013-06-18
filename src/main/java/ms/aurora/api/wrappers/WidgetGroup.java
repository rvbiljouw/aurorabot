package ms.aurora.api.wrappers;

import ms.aurora.rt3.IWidget;

/**
 * @author Rick
 */
public class WidgetGroup {
    private final Widget[] widgets;
    private final int index;

    public WidgetGroup(IWidget[] widgets, int index) {
        this.index = index;
        if (widgets != null) {
            this.widgets = new Widget[widgets.length];

            for (int child = 0; child < widgets.length; child++) {
                IWidget widget = widgets[child];
                if (widget != null) {
                    this.widgets[child] = new Widget(widget, index, child);
                }
            }
        } else {
            this.widgets = new Widget[0];
        }
    }

    public boolean isValid() {
        return widgets.length > 0 && widgets[0] != null;
    }

    public Widget[] getWidgets() {
        return widgets;
    }

    public int getIndex() {
        return index;
    }
}
