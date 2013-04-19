package ms.aurora.api.methods.tabs;

import ms.aurora.api.methods.Widgets;
import ms.aurora.api.wrappers.RSWidget;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

/**
 * @author rvbiljouw
 */
public class Equipment {
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

    private static RSWidget getEquipmentWidget() {
        return Widgets.getWidget(387, 28);
    }

    /**
     * Gets all equipped items.
     * @return array of equipped items.
     */
    public static Item[] getItems() {
        List<Item> items = newArrayList();
        RSWidget widget = getEquipmentWidget();
        int[] ids = widget.getInventoryItems();
        int[] amounts = widget.getInventoryStackSizes();
        for (int slot = 0; slot < ids.length; slot++) {
            Item item = new Item(ids[slot], amounts[slot]);
            items.add(item);
        }
        return items.toArray(new Item[items.size()]);
    }

    /**
     * Checks whether an item matching the supplied ids is equipped.
     * @param ids array of ids to check.
     * @return true if there is a match else false.
     */
    public static boolean isEquipped(int... ids) {
        for (Item item : getItems()) {
            for (int id : ids)
                if (item.getId() == id) return true;
        }
        return false;
    }

    /**
     * Checks whether the item with the corresponding id is equipped.
     * and that there is the specified amount equipped.
     * @param id id of item to check.
     * @param amount amount of item equipped.
     * @return true if item is equipped else false.
     */
    public static boolean isEquipped(int id, int amount) {
        for (Item item : getItems()) {
            if (item.getId() == id && item.getStackSize() >= amount) {
                return true;
            }
        }
        return false;
    }

    public static RSWidget getSlot(int id) {
        return Widgets.getWidget(387, id);
    }

    public static class Item {
        private final int id;
        private final int stackSize;

        public Item(int id, int stackSize) {
            this.id = id - 1;
            this.stackSize = stackSize;
        }

        public int getId() {
            return id;
        }

        public int getStackSize() {
            return stackSize;
        }
    }

}

