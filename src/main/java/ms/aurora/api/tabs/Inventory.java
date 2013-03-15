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

/**
 * @author rvbiljouw
 */
public class Inventory {
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
    public boolean isFull() {
        return getAll().length == 28;
    }

    /**
     * Retrieves the first item that matches the predicate.
     *
     * @param predicate Predicate to match items against
     * @return the first matching item, or null if none were found.
     */
    public static Item get(final Predicate<Item> predicate) {
        Item[] items = getAll(predicate);
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
    public static Item[] getAll(final Predicate<Item> predicate) {
        return filter(newArrayList(getAll()),
                new com.google.common.base.Predicate<Item>() {
                    @Override
                    public boolean apply(@Nullable Item item) {
                        return predicate.apply(item);
                    }
                }
        ).toArray(new Item[0]);
    }

    /**
     * Retrieves all the items in the inventory
     *
     * @return an array containing all items in the inventory.
     */
    public static Item[] getAll() {
        RSWidget inventory = getInventoryWidget();
        int[] items = inventory.getInventoryItems();
        int[] stacks = inventory.getInventoryStackSizes();
        List<Item> wrappers = newArrayList();

        for (int i = 0; i < items.length; i++) {
            if (items[i] > 0 && stacks[i] > 0) {
                Item item = new Item(items[i], stacks[i]);
                item.slot = i;
                wrappers.add(item);
            }
        }
        return wrappers.toArray(new Item[wrappers.size()]);
    }

    /**
     * A class encapsulating inventory items.
     */
    public static class Item {
        protected int slot;
        private int id;
        private int stackSize;

        public Item(int id) {
            this(id, 1);
        }

        public Item(int id, int stackSize) {
            this.id = id;
            this.stackSize = stackSize;
        }

        public int getId() {
            return id;
        }

        public int getStackSize() {
            return stackSize;
        }

        private Point getLocation() {
            RSWidget inventory = getInventoryWidget();
            int col = (slot % 4);
            int row = (slot / 4);
            return new Point(inventory.getX() + (col * 42),
                    inventory.getY() + (row * 36));
        }

        public void applyAction(String action) {
            VirtualMouse mouse = ClientContext.get().input.getMouse();
            Point location = getLocation();
            mouse.moveMouse(location.x, location.y);
            Menu.click(action);
        }
    }

}
