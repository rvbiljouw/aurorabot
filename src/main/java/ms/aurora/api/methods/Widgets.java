package ms.aurora.api.methods;

import com.google.common.base.Function;
import com.google.common.base.Predicates;
import ms.aurora.api.ClientContext;
import ms.aurora.api.wrappers.RSWidget;
import ms.aurora.api.wrappers.RSWidgetGroup;
import ms.aurora.rt3.Widget;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.collect.Collections2.filter;
import static com.google.common.collect.Collections2.transform;
import static com.google.common.collect.Lists.newArrayList;

/**
 * @author tobiewarburton
 * @author rvbiljouw
 */
public final class Widgets {
    private final ClientContext ctx;

    public Widgets(ClientContext ctx) {
        this.ctx = ctx;
    }

    public RSWidgetGroup[] getAll() {
        Widget[][] cache = ctx.getClient().getWidgetCache();
        if (cache == null) return new RSWidgetGroup[0];

        List<RSWidgetGroup> groups = newArrayList();
        for(int i = 0; i < cache.length; i++) {
            RSWidgetGroup group = getWidgets(i);
            if(group != null) {
                groups.add(group);
            }
        }
        return groups.toArray(new RSWidgetGroup[]{});
    }

    public RSWidget getWidget(int parent, int child) {
        RSWidgetGroup group = getWidgets(parent);
        if (group != null && group.getWidgets()[child] != null) {
            return group.getWidgets()[child];
        }
        return null;
    }

    public RSWidgetGroup getWidgets(int parent) {
        if (ctx.getClient().getWidgetCache()[parent] != null) {
            return new RSWidgetGroup(ctx, ctx.getClient().getWidgetCache()[parent], parent);
        }
        return null;
    }

    /**
     * @param predicate the string to search for
     * @return a list of all the RSWidget that contain the specified text
     */
    public RSWidget[] getWidgetsWithText(String predicate) {
        List<RSWidget> satisfied = new ArrayList<RSWidget>();
        for (Widget[] parents : ctx.getClient().getWidgetCache()) {
            if(parents == null) continue;

            for (RSWidget child : filter(transform(newArrayList(parents), transform), Predicates.notNull()).toArray(new RSWidget[0])) {
                if (child.getText() != null && child.getText().contains(predicate)) {
                    satisfied.add(child);
                }
            }
        }
        return satisfied.toArray(new RSWidget[0]);
    }

    public boolean canContinue() {
        RSWidget[] possible = getWidgetsWithText("Click here to continue");
        return possible.length > 0;
    }

    public void clickContinue() {
        for (RSWidget widget : getWidgetsWithText("Click here to continue")) {
            widget.click(true);
        }
    }

    private final Function<Widget, RSWidget> transform = new Function<Widget, RSWidget>() {
        @Override
        public RSWidget apply(Widget widget) {
            if (widget != null) {
                return new RSWidget(ctx, widget, 0, 0);
            }
            return null;
        }
    };

}
