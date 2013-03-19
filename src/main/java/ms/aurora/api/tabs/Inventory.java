package ms.aurora.api.tabs;

import com.sun.istack.internal.Nullable;
import ms.aurora.api.ClientContext;
import ms.aurora.api.Menu;
import ms.aurora.api.Widgets;
import ms.aurora.api.util.Predicate;
import ms.aurora.api.wrappers.RSWidget;
import ms.aurora.input.VirtualMouse;

import java.awt.*;
import java.util.List;

import static com.google.common.collect.Collections2.filter;
import static com.google.common.collect.Lists.newArrayList;
import static ms.aurora.api.util.Utilities.sleepNoException;

/**
 * @author rvbiljouw
 */
public class Inventory {
    private static final int INVENTORY_ID = 149;

    private Inventory() { }

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
    public static InventoryItem get(final Predicate<InventoryItem> predicate) {
        InventoryItem[] inventoryItems = getAll(predicate);
        if (inventoryItems.length > 0) {
            return inventoryItems[0];
        }
        return null;
    }

    /**
     * Retrieves the first item that matches the specified ID.
     *
     * @param id InventoryItem ID to look for
     * @return item if it was found, otherwise null.
     */
    public static InventoryItem get(int id) {
        for (InventoryItem inventoryItem : getAll()) {
            if (inventoryItem.getId() == id) {
                return inventoryItem;
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
    public static InventoryItem[] getAll(final Predicate<InventoryItem> predicate) {
        return filter(newArrayList(getAll()),
                new com.google.common.base.Predicate<InventoryItem>() {
                    @Override
                    public boolean apply(@Nullable InventoryItem inventoryItem) {
                        return predicate.apply(inventoryItem);
                    }
                }
        ).toArray(new InventoryItem[0]);
    }

    /**
     * Retrieves all items that match the specified ID.
     *
     * @param id InventoryItem ID to look for
     * @return list of items found, which can be empty.
     */
    public static InventoryItem[] getAll(int id) {
        List<InventoryItem> inventoryItems = newArrayList();
        for (InventoryItem inventoryItem : getAll()) {
            if (inventoryItem.getId() == id) {
                inventoryItems.add(inventoryItem);
            }
        }
        return inventoryItems.toArray(new InventoryItem[inventoryItems.size()]);
    }

    /**
     * Retrieves all the items in the inventory
     *
     * @return an array containing all items in the inventory.
     */
    public static InventoryItem[] getAll() {
        RSWidget inventory = getInventoryWidget();
        int[] items = inventory.getInventoryItems();
        int[] stacks = inventory.getInventoryStackSizes();
        List<InventoryItem> wrappers = newArrayList();

        for (int i = 0; i < items.length; i++) {
            if (items[i] > 0 && stacks[i] > 0) {
                InventoryItem inventoryItem = new InventoryItem(items[i] - 1, stacks[i]);
                inventoryItem.slot = i;
                wrappers.add(inventoryItem);
            }
        }
        return wrappers.toArray(new InventoryItem[wrappers.size()]);
    }

    /**
     * Checks if the inventory contains a specific item
     *
     * @param id InventoryItem to look for
     * @return true if found, otherwise false.
     */
    public static boolean contains(int id) {
        for (InventoryItem inventoryItem : getAll()) {
            if (inventoryItem.getId() == id) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if the inventory contains at least the
     * specified amount of the specified item,
     *
     * @param id     InventoryItem to count
     * @param amount Minimum amount to pass.
     * @return true if the inventory contains at least the amount specified.
     */
    public static boolean containsMinimum(int id, int amount) {
        for (InventoryItem inventoryItem : getAll()) {
            if (inventoryItem.getId() == id && inventoryItem.getStackSize() >= amount) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if the inventory contains at most the
     * specified amount of the specified item,
     *
     * @param id     InventoryItem to count
     * @param amount Maximum amount to pass.
     * @return true if the inventory contains at most the amount specified.
     */
    public static boolean containsMaximum(int id, int amount) {
        for (InventoryItem inventoryItem : getAll()) {
            if (inventoryItem.getId() == id && inventoryItem.getStackSize() <= amount) {
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
     * @param id InventoryItem ID of the items to count
     * @return total amount of items matching id in inventory.
     */
    public static int count(int id) {
        int count = 0;
        for (InventoryItem inventoryItem : getAll()) {
            if (inventoryItem.getId() == id) {
                count += inventoryItem.getStackSize();
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
        InventoryItem firstMatch = get(id);
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
        InventoryItem[] matches = getAll(id);
        for (InventoryItem match : matches) {
            match.applyAction("Drop");
        }
    }

    /**
     * Drops all items matching any of the IDs specified.
     *
     * @param ids A var-args list of IDs to drop.
     */
    public static void dropAll(int... ids) {
        for (int id : ids) {
            dropAll(id);
        }
    }

    /**
     * Drops all the items NOT matching any of the IDs specified
     *
     * @param ids A var-args list of items to exclude from dropping.
     */
    public static void dropAllExcept(int... ids) {
        for (InventoryItem inventoryItem : getAll()) {
            boolean drop = true;
            for (int id : ids) {
                if (inventoryItem.getId() == id) drop = false;
            }

            if (drop) {
                inventoryItem.applyAction("Drop");
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
        InventoryItem main = get(id);
        if (main != null) {
            InventoryItem[] targets = getAll(targetId);
            for (InventoryItem target : targets) {
                main.applyAction("Use");
                sleepNoException(140, 200);
                target.click();
                sleepNoException(500, 800);
                while (ClientContext.get().getMyPlayer().getAnimation() != -1) {
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
    public static void useItemOn(int id, int targetId) {
        InventoryItem main = get(id);
        if (main != null) {
            InventoryItem[] targets = getAll(targetId);
            for (InventoryItem target : targets) {
                main.applyAction("Use");
                sleepNoException(140, 200);
                target.click();
                sleepNoException(500, 800);
                while (ClientContext.get().getMyPlayer().getAnimation() != -1) {
                    sleepNoException(140, 200);
                }
                sleepNoException(100, 120);
                return;
            }
        }
    }

    /**
     * A class encapsulating inventory items.
     */
    public static class InventoryItem {
        private int slot;
        private int id;
        private int stackSize;

        public InventoryItem(int id) {
            this(id, 1);
        }

        public InventoryItem(int id, int stackSize) {
            this.id = id;
            this.stackSize = stackSize;
        }

        public int getId() {
            return id;
        }

        public int getStackSize() {
            return stackSize;
        }

        public Rectangle getArea() {
            int col = (slot % 4);
            int row = (slot / 4);
            int x = 580 + (col * 42);
            int y = 228 + (row * 36);

            return new Rectangle(x - (36 / 2), y - (32 / 2), 36, 32);
        }

        public void applyAction(String action) {
            VirtualMouse mouse = ClientContext.get().input.getMouse();
            Rectangle area = getArea();
            mouse.moveMouse((int) area.getCenterX(), (int) area.getCenterY());
            Menu.click(action);
        }

        public void click() {
            VirtualMouse mouse = ClientContext.get().input.getMouse();
            Rectangle area = getArea();
            mouse.moveMouse((int) area.getCenterX(), (int) area.getCenterY());
            mouse.clickMouse(true);
        }
    }

}
