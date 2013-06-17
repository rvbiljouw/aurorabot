package ms.aurora.api.methods;

import ms.aurora.api.Context;
import ms.aurora.api.wrappers.Widget;
import ms.aurora.api.wrappers.WidgetGroup;
import ms.aurora.rt3.IClient;
import ms.aurora.rt3.IWidget;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Widget related functions
 *
 * @author tobiewarburton
 * @author Rick
 */
public final class Widgets {

    /**
     * Gets an array of all the NON-NULL widget groups in the client.
     *
     * @return widget groups
     */
    public static WidgetGroup[] getAll() {
        IWidget[][] cache = getClient().getWidgetCache();
        if (cache == null) return new WidgetGroup[0];

        List<WidgetGroup> groups = new ArrayList<WidgetGroup>();
        for (int i = 0; i < cache.length; i++) {
            WidgetGroup group = getWidgetGroup(i);
            if (group != null) {
                groups.add(group);
            }
        }
        return groups.toArray(new WidgetGroup[groups.size()]);
    }

    /**
     * Gets a specific widget from the widget cache, which may be null.
     *
     * @param parent parent index
     * @param child  child index
     * @return widget or null
     */
    public static Widget getWidget(int parent, int child) {
        WidgetGroup group = getWidgetGroup(parent);
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
    public static WidgetGroup getWidgetGroup(int parent) {
        if (getClient().getWidgetCache()[parent] != null) {
            return new WidgetGroup(getClient().getWidgetCache()[parent], parent);
        }
        return null;
    }

    /**
     * Gets all widgets containing the specified text
     *
     * @param predicate the string to search for
     * @return a list of all the RSWidget that contain the specified text
     */
    public static Widget[] getWidgetsWithText(String predicate) {
        final Rectangle rect = new Rectangle(5, 350, 510, 130);
        List<Widget> satisfied = new ArrayList<Widget>();
        for (WidgetGroup groups : getAll()) {
            for (Widget child : groups.getWidgets()) {
                if (child != null && child.getText().contains(predicate) &&
                        rect.contains(child.getCenterPoint())) {
                    satisfied.add(child);
                }
            }
        }
        return satisfied.toArray(new Widget[satisfied.size()]);
    }

    /**
     * Checks whether the "Click here to continue" interface is up.
     *
     * @return true if it's up
     */
    public static boolean canContinue() {
        Widget[] possible = getWidgetsWithText("Click here to continue");
        return possible.length > 0;
    }

    /**
     * Clicks the "Click here to continue" interface.
     */
    public static void clickContinue() {
        for (Widget widget : getWidgetsWithText("Click here to continue")) {
            widget.click(true);
        }
    }

    /**
     * INTERNAL: Retrieves the client for the current context.
     *
     * @return client
     */
    private static IClient getClient() {
        return Context.getClient();
    }
}
