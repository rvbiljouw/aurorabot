package ms.aurora.api.methods.tabs;

import ms.aurora.api.methods.Players;
import ms.aurora.api.methods.Widgets;
import ms.aurora.api.methods.filters.WidgetItemFilters;
import ms.aurora.api.util.ArrayUtils;
import ms.aurora.api.util.Predicate;
import ms.aurora.api.util.StatePredicate;
import ms.aurora.api.util.Utilities;
import ms.aurora.api.wrappers.Widget;
import ms.aurora.api.wrappers.WidgetItem;

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
    private static Widget getInventoryWidget() {
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
    public static WidgetItem get(final Predicate<WidgetItem>... predicate) {
        WidgetItem[] items = getAll(predicate);
        if (items.length > 0) {
            return items[0];
        }
        return null;
    }

    /**
     * Retrieves all items that match the supplied predicate.
     *
     * @param predicate Predicate to match items against.
     * @return An array of all matching items (can be empty).
     */
    public static WidgetItem[] getAll(final Predicate<WidgetItem>... predicate) {
        List<WidgetItem> filter = ArrayUtils.filter(getAll(), predicate);
        return filter.toArray(new WidgetItem[filter.size()]);
    }


    public static WidgetItem getItemAt(int slot) {
        return _getAll()[slot - 1];
    }

    /**
     * Retrieves all the items in the inventory
     *
     * @return an array containing all items in the inventory.
     */
    private static WidgetItem[] _getAll() {
        Widget inventory = getInventoryWidget();
        int[] items = inventory.getInventoryItems();
        int[] stacks = inventory.getInventoryStackSizes();
        List<WidgetItem> wrappers = new ArrayList<WidgetItem>();

        for (int i = 0; i < items.length; i++) {
            if (items[i] > 0 && stacks[i] > 0) {
                int col = (i % 4);
                int row = (i / 4);
                int x = inventory.getX() + (col * 42);
                int y = inventory.getY() + (row * 36);

                Rectangle area = new Rectangle(x, y, 31, 31);
                WidgetItem item = new WidgetItem(area, items[i] - 1, stacks[i]);
                wrappers.add(item);
            } else {
                wrappers.add(null);
            }
        }
        return wrappers.toArray(new WidgetItem[wrappers.size()]);
    }

    public static WidgetItem[] getAll() {
        return ArrayUtils.filter(_getAll(), new Predicate<WidgetItem>() {
            @Override
            public boolean apply(WidgetItem object) {
                return object != null;
            }
        }).toArray(new WidgetItem[]{});
    }

    /**
     * Checks if the inventory contains a specific item
     *
     * @param predicates RSWidgetItem to look for
     * @return true if found, otherwise false.
     */
    public static boolean contains(Predicate<WidgetItem>... predicates) {
        return getAll(predicates).length > 0;
    }

    /**
     * Checks if the inventory contains all of the specified items
     *
     * @param predicates A var-args list of item predicates.
     * @return true if all the items were found, false otherwise.
     */
    public static boolean containsAll(Predicate<WidgetItem>... predicates) {
        for (Predicate<WidgetItem> predicate : predicates) {
            if (!contains(predicate)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Counts all the items matching the specified ID.
     *
     * @param predicates RSWidgetItem predicates of the items to count
     * @return total amount of items matching id in inventory.
     */
    public static int count(Predicate<WidgetItem>... predicates) {
        int count = 0;
        for (WidgetItem item : getAll(predicates)) {
            count += item.getStackSize();
        }
        return count;
    }

    /**
     * Drops one item with the specified ID.
     *
     * @param predicates Predicates of the item to drop.
     */
    public static void dropItem(Predicate<WidgetItem>... predicates) {
        WidgetItem firstMatch = get(predicates);
        if (firstMatch != null) {
            firstMatch.applyAction("Drop");
        }
    }

    /**
     * Drops all items matching any of the IDs specified.
     *
     * @param predicates A var-args list of predicates to drop.
     */
    public static void dropAll(Predicate<WidgetItem>... predicates) {
        for (int i = 1; i < 29 && !Thread.currentThread().isInterrupted();) {
            WidgetItem item = getItemAt(i);
            boolean drop = false;
            for (Predicate<WidgetItem> predicate: predicates) {
                if (item != null && predicate.apply(item)) {
                    drop = true;
                }
            }
            if (item == null || !drop) {
                i++;
            } else {
                if (item.applyAction("Drop")) {
                    if (Utilities.sleepUntil(EMPTY(i), 2000)) {
                        i++;
                    }
                }
            }
        }
    }

    /**
     * Drops all the items NOT matching any of the IDs specified
     *
     * @param predicates A var-args list of items to exclude from dropping.
     */
    public static void dropAllExcept(Predicate<WidgetItem>... predicates) {
        for (int i = 1; i < 29 && !Thread.currentThread().isInterrupted();) {
            WidgetItem item = getItemAt(i);
            boolean drop = true;
            for (Predicate<WidgetItem> predicate: predicates) {
                if (item != null && predicate.apply(item)) {
                    drop = false;
                }
            }
            if (item == null || !drop) {
                i++;
            } else {
                if (item.applyAction("Drop")) {
                    if (Utilities.sleepUntil(EMPTY(i), 2000)) {
                        i++;
                    }
                }
            }
        }
    }

    /**
     * Uses one item on all items matching the specified target ID.
     *
     * @param id       ID of the main item
     * @param targetId ID of the target item
     */
    public static void useItemOnAll(int id, int targetId) { // TODO - switch to predicates
        WidgetItem main = get(WidgetItemFilters.ID(id));
        if (main != null) {
            WidgetItem[] targets = getAll(WidgetItemFilters.ID(targetId));
            for (WidgetItem target : targets) {
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
    public static void useItemOn(int id, int targetId) {  // TODO - switch to predicates
        WidgetItem main = get(WidgetItemFilters.ID(id));
        if (main != null) {
            WidgetItem[] targets = getAll(WidgetItemFilters.ID(targetId));
            for (WidgetItem target : targets) {
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


    public static void dropAllByColumn(Predicate<WidgetItem>... predicates) {
        for (int column = 1; column < 5 && !Thread.currentThread().isInterrupted(); column++) {
            for (int slot = 0; (slot + column) < 29 && !Thread.currentThread().isInterrupted();) {
                WidgetItem item = getItemAt(slot + column);
                boolean drop = false;
                for (Predicate<WidgetItem> predicate: predicates) {
                    if (item != null && predicate.apply(item)) {
                        drop = true;
                    }
                }
                if (item == null || !drop) {
                    slot += 4;
                } else {
                    if (item.applyAction("Drop")) {
                        if (Utilities.sleepUntil(EMPTY(slot + column), 2000)) {
                            slot += 4;
                        }
                    }
                }
            }
        }
    }

    private static StatePredicate EMPTY(final int slot) {
        return new StatePredicate() {
            @Override
            public boolean apply() {
                return getItemAt(slot) == null;
            }
        };
    }

}
