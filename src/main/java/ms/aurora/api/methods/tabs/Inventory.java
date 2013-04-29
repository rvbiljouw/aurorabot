package ms.aurora.api.methods.tabs;

import ms.aurora.api.methods.Players;
import ms.aurora.api.methods.Widgets;
import ms.aurora.api.util.ArrayUtils;
import ms.aurora.api.util.Predicate;
import ms.aurora.api.wrappers.RSWidget;
import ms.aurora.api.wrappers.RSWidgetItem;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static ms.aurora.api.util.Utilities.sleepNoException;

/**
 * @author Rick
 * @author tobiewarburton
 */
public final class Inventory {
    private static final int INVENTORY_ID = 149;

    /**
     * Retrieves the inventory widget
     *
     * @return inventory
     */
    private static RSWidget getInventoryWidget() {
        return Widgets.getWidget(INVENTORY_ID, 0);
    }

    /**
     * Checks if the inventory is full
     *
     * @return true if inventory contains 28 items, false otherwise.
     */
    public static boolean isFull() {
        return getAll().length == 28;
    }

    /**
     * Retrieves the amount of inventory slots currently occupied.
     *
     * @return count
     */
    public static int getCount() {
        return getAll().length;
    }

    /**
     * Retrieves the first item that matches the predicate.
     *
     * @param predicate Predicate to match items against
     * @return the first matching item, or null if none were found.
     */
    public static RSWidgetItem get(final Predicate<RSWidgetItem> predicate) {
        RSWidgetItem[] items = getAll(predicate);
        if (items.length > 0) {
            return items[0];
        }
        return null;
    }

    /**
     * Retrieves the first item that matches the specified ID.
     *
     * @param id RSWidgetItem ID to look for
     * @return item if it was found, otherwise null.
     */
    public static RSWidgetItem get(int id) {
        for (RSWidgetItem item : getAll()) {
            if (item.getId() == id) {
                return item;
            }
        }
        return null;
    }

    /**
     * Retrieves the first item that matches one od the specified Ids.
     *
     * @param ids RSWidgetItem ID to look for
     * @return item if it was found, otherwise null.
     */
    public static RSWidgetItem get(int... ids) {
        for (RSWidgetItem item : getAll()) {
            for (int id : ids) {
                if (item.getId() == id) {
                    return item;
                }
            }
        }
        return null;
    }

    /**
     * Retrieves all items that match the supplied predicate.
     *
     * @param predicate Predicate to match items against.
     * @return An array of all matching items (can be empty).
     */
    public static RSWidgetItem[] getAll(final Predicate<RSWidgetItem> predicate) {
        return ArrayUtils.filter(getAll(), predicate).toArray(new RSWidgetItem[0]);
    }

    /**
     * Retrieves all items that match the specified ID.
     *
     * @param id RSWidgetItem ID to look for
     * @return list of items found, which can be empty.
     */
    public static RSWidgetItem[] getAll(int id) {
        List<RSWidgetItem> items = new ArrayList<RSWidgetItem>();
        for (RSWidgetItem item : getAll()) {
            if (item.getId() == id) {
                items.add(item);
            }
        }
        return items.toArray(new RSWidgetItem[items.size()]);
    }

    /**
     * Retrieves all items that match the specified IDs.
     *
     * @param ids RSWidgetItem IDs to look for
     * @return list of items found, which can be empty.
     */
    public static RSWidgetItem[] getAll(int... ids) {
        List<RSWidgetItem> items = new ArrayList<RSWidgetItem>();
        for (RSWidgetItem item : getAll()) {
            for (int id : ids) {
                if (item.getId() == id) {
                    items.add(item);
                }
            }
        }
        return items.toArray(new RSWidgetItem[items.size()]);
    }

    /**
     * Retrieves all the items in the inventory
     *
     * @return an array containing all items in the inventory.
     */
    public static RSWidgetItem[] getAll() {
        RSWidget inventory = getInventoryWidget();
        int[] items = inventory.getInventoryItems();
        int[] stacks = inventory.getInventoryStackSizes();
        List<ms.aurora.api.wrappers.RSWidgetItem> wrappers = new ArrayList<ms.aurora.api.wrappers.RSWidgetItem>();

        for (int i = 0; i < items.length; i++) {
            if (items[i] > 0 && stacks[i] > 0) {
                int col = (i % 4);
                int row = (i / 4);
                int x = 580 + (col * 42);
                int y = 228 + (row * 36);

                Rectangle area = new Rectangle(x - (36 / 2), y - (32 / 2), 36, 32);
                ms.aurora.api.wrappers.RSWidgetItem item = new ms.aurora.api.wrappers.RSWidgetItem(area, items[i] - 1, stacks[i]);
                wrappers.add(item);
            }
        }
        return wrappers.toArray(new ms.aurora.api.wrappers.RSWidgetItem[wrappers.size()]);
    }

    /**
     * Checks if the inventory contains a specific item
     *
     * @param id RSWidgetItem to look for
     * @return true if found, otherwise false.
     */
    public static boolean contains(int id) {
        for (RSWidgetItem item : getAll()) {
            if (item.getId() == id) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if the inventory contains at least the
     * specified amount of the specified item,
     *
     * @param id     RSWidgetItem to count
     * @param amount Minimum amount to pass.
     * @return true if the inventory contains at least the amount specified.
     */
    public static boolean containsMinimum(int id, int amount) {
        for (RSWidgetItem item : getAll()) {
            if (item.getId() == id && item.getStackSize() >= amount) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if the inventory contains at most the
     * specified amount of the specified item,
     *
     * @param id     RSWidgetItem to count
     * @param amount Maximum amount to pass.
     * @return true if the inventory contains at most the amount specified.
     */
    public static boolean containsMaximum(int id, int amount) {
        for (RSWidgetItem item : getAll()) {
            if (item.getId() == id && item.getStackSize() <= amount) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if the inventory contains at least one
     * of the specified items
     *
     * @param ids A var-args list of item IDs.
     * @return true if found, otherwise false.
     */
    public static boolean containsAny(int... ids) {
        for (int id : ids) {
            if (contains(id)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if the inventory contains all of the specified items
     *
     * @param ids A var-args list of item IDs.
     * @return true if all the items were found, false otherwise.
     */
    public static boolean containsAll(int... ids) {
        for (int id : ids) {
            if (!contains(id)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Counts all the items matching the specified ID.
     *
     * @param ids RSWidgetItem ids of the items to count
     * @return total amount of items matching id in inventory.
     */
    public static int count(int... ids) {
        int count = 0;
        for (RSWidgetItem item : getAll()) {
            for (int id : ids) {
                if (item.getId() == id) {
                    count += item.getStackSize();
                }
            }
        }
        return count;
    }

    /**
     * Drops one item with the specified ID.
     *
     * @param id ID of the item to drop.
     */
    public static void dropItem(int id) {
        RSWidgetItem firstMatch = get(id);
        if (firstMatch != null) {
            firstMatch.applyAction("Drop");
        }
    }

    /**
     * Drops all items with the specified ID.
     *
     * @param id ID of the item to drop.
     */
    public static void dropAll(int id) {
        RSWidgetItem[] matches = getAll(id);
        for (RSWidgetItem match : matches) {
            match.applyAction("Drop");
        }
    }

    /**
     * Drops all items matching any of the IDs specified.
     *
     * @param ids A var-args list of IDs to drop.
     */
    public static void dropAll(int... ids) {
        for (RSWidgetItem item : getAll()) {
            for (int id : ids) {
                if (item.getId() == id) {
                    item.applyAction("Drop");
                }
            }
        }
    }

    /**
     * Drops all the items NOT matching any of the IDs specified
     *
     * @param ids A var-args list of items to exclude from dropping.
     */
    public static void dropAllExcept(int... ids) {
        for (RSWidgetItem item : getAll()) {
            boolean drop = true;
            for (int id : ids) {
                if (item.getId() == id) drop = false;
            }

            if (drop) {
                item.applyAction("Drop");
            }
        }
    }

    /**
     * Uses one item on all items matching the specified target ID.
     *
     * @param id       ID of the main item
     * @param targetId ID of the target item
     */
    public static void useItemOnAll(int id, int targetId) {
        RSWidgetItem main = get(id);
        if (main != null) {
            RSWidgetItem[] targets = getAll(targetId);
            for (RSWidgetItem target : targets) {
                main.applyAction("Use");
                sleepNoException(140, 200);
                target.click(true);
                sleepNoException(500, 800);
                while (Players.getLocal().getAnimation() != -1) {
                    sleepNoException(140, 200);
                }
                sleepNoException(100, 120);
            }
        }
    }

    /**
     * Uses one item on an item matching the specified target ID.
     *
     * @param id       ID of the main item
     * @param targetId ID of the target item
     */
    public void useItemOn(int id, int targetId) {
        RSWidgetItem main = get(id);
        if (main != null) {
            RSWidgetItem[] targets = getAll(targetId);
            for (RSWidgetItem target : targets) {
                main.applyAction("Use");
                sleepNoException(140, 200);
                target.click(true);
                sleepNoException(500, 800);
                while (Players.getLocal().getAnimation() != -1) {
                    sleepNoException(140, 200);
                }
                sleepNoException(100, 120);
                return;
            }
        }
    }
}
