package ms.aurora.api.methods;

import com.google.common.base.Function;
import com.google.common.base.Predicates;
import ms.aurora.api.Context;
import ms.aurora.api.wrappers.RSWidget;
import ms.aurora.api.wrappers.RSWidgetGroup;
import ms.aurora.rt3.Client;
import ms.aurora.rt3.Widget;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.collect.Collections2.filter;
import static com.google.common.collect.Collections2.transform;
import static com.google.common.collect.Lists.newArrayList;

/**
 * Widget related functions
 *
 * @author tobiewarburton
 * @author rvbiljouw
 */
public final class Widgets {

    /**
     * Gets an array of all the NON-NULL widget groups in the client.
     *
     * @return widget gruop;s
     */
    public static RSWidgetGroup[] getAll() {
        Widget[][] cache = getClient().getWidgetCache();
        if (cache == null) return new RSWidgetGroup[0];

        List<RSWidgetGroup> groups = newArrayList();
        for (int i = 0; i < cache.length; i++) {
            RSWidgetGroup group = getWidgets(i);
            if (group != null) {
                groups.add(group);
            }
        }
        return groups.toArray(new RSWidgetGroup[]{});
    }

    /**
     * Gets a specific widget from the widget cache, which may be null.
     *
     * @param parent parent index
     * @param child  child index
     * @return
     */
    public static RSWidget getWidget(int parent, int child) {
        RSWidgetGroup group = getWidgets(parent);
        if (group != null && group.getWidgets()[child] != null) {
            return group.getWidgets()[child];
        }
        return null;
    }

    /**
     * Retrieves a widget group by it's index
     *
     * @param parent widget group index
     * @return widget group or null
     */
    public static RSWidgetGroup getWidgets(int parent) {
        if (getClient().getWidgetCache()[parent] != null) {
            return new RSWidgetGroup(Context.get(), getClient().getWidgetCache()[parent], parent);
        }
        return null;
    }

    /**
     * Gets all widgets containing the specified text
     *
     * @param predicate the string to search for
     * @return a list of all the RSWidget that contain the specified text
     */
    public static RSWidget[] getWidgetsWithText(String predicate) {
        List<RSWidget> satisfied = new ArrayList<RSWidget>();
        for (Widget[] parents : getClient().getWidgetCache()) {
            if (parents == null) continue;

            for (RSWidget child : filter(transform(newArrayList(parents), transform), Predicates.notNull()).toArray(new RSWidget[0])) {
                if (child.getText() != null && child.getText().contains(predicate)) {
                    satisfied.add(child);
                }
            }
        }
        return satisfied.toArray(new RSWidget[0]);
    }

    /**
     * Checks whether the "Click here to continue" interface is up.
     *
     * @return true if it's up
     */
    public static boolean canContinue() {
        RSWidget[] possible = getWidgetsWithText("Click here to continue");
        return possible.length > 0;
    }

    /**
     * Clicks the "Click here to continue" interface.
     */
    public static void clickContinue() {
        for (RSWidget widget : getWidgetsWithText("Click here to continue")) {
            widget.click(true);
        }
    }

    /**
     * INTERNAL: Retrieves the client for the current context.
     *
     * @return client
     */
    private static Client getClient() {
        return Context.get().getClient();
    }

    /**
     * Transforms an unwrapped widget into a wrapped one.
     */
    private static final Function<Widget, RSWidget> transform = new Function<Widget, RSWidget>() {
        @Override
        public RSWidget apply(Widget widget) {
            if (widget != null) {
                return new RSWidget(Context.get(), widget, 0, 0);
            }
            return null;
        }
    };
}
