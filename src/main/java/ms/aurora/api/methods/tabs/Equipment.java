package ms.aurora.api.methods.tabs;

import ms.aurora.api.methods.Widgets;
import ms.aurora.api.util.ArrayUtils;
import ms.aurora.api.util.Predicate;
import ms.aurora.api.wrappers.Widget;
import ms.aurora.api.wrappers.WidgetItem;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author rvbiljouw
 */
public final class Equipment {
    public static final int EQUIPMENT_HEAD = 12;
    public static final int EQUIPMENT_CAPE = 13;
    public static final int EQUIPMENT_NECK = 14;
    public static final int EQUIPMENT_QUIVER = 15;
    public static final int EQUIPMENT_RIGHT_HAND = 16;
    public static final int EQUIPMENT_TORSO = 17;
    public static final int EQUIPMENT_LEFT_HAND = 18;
    public static final int EQUIPMENT_LEGS = 19;
    public static final int EQUIPMENT_FEET = 20;
    public static final int EQUIPMENT_HANDS = 21;
    public static final int EQUIPMENT_RING = 22;

    private static Widget getEquipmentWidget() {
        return Widgets.getWidget(387, 28);
    }

    /**
     * Gets all equipped items.
     * @return array of equipped items.
     */
    public static WidgetItem[] getItems() {
        List<WidgetItem> items = new ArrayList<WidgetItem>();
        Widget widget = getEquipmentWidget();
        int[] ids = widget.getInventoryItems();
        int[] amounts = widget.getInventoryStackSizes();
        for (int slot = 0; slot < ids.length; slot++) {
            WidgetItem item = new WidgetItem(new Rectangle(), ids[slot] - 1, amounts[slot]); // TODO - work out rectangles
            items.add(item);
        }
        return items.toArray(new WidgetItem[items.size()]);
    }

    /**
     * Checks whether an item matching the supplied ids is equipped.
     * @param predicates array of predicates to check.
     * @return true if there is a match else false.
     */
    public static boolean isEquipped(Predicate<WidgetItem>... predicates) {
        return ArrayUtils.filter(getItems(), predicates).size() > 0;
    }

    public static Widget getSlot(int id) {
        return Widgets.getWidget(387, id);
    }

}

