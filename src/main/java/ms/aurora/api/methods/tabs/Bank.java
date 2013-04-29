package ms.aurora.api.methods.tabs;

import ms.aurora.api.Context;
import ms.aurora.api.methods.Menu;
import ms.aurora.api.methods.Npcs;
import ms.aurora.api.methods.Objects;
import ms.aurora.api.methods.Widgets;
import ms.aurora.api.methods.filters.NpcFilters;
import ms.aurora.api.methods.filters.ObjectFilters;
import ms.aurora.api.util.ArrayUtils;
import ms.aurora.api.util.Predicate;
import ms.aurora.api.wrappers.Interactable;
import ms.aurora.api.wrappers.RSWidget;
import ms.aurora.api.wrappers.RSWidgetItem;
import ms.aurora.input.VirtualMouse;
import ms.aurora.rt3.Mouse;
import org.apache.log4j.Logger;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static ms.aurora.api.util.Utilities.sleepNoException;

/**
 * @author tobiewarburton
 */
public final class Bank {
    private static final Logger logger = Logger.getLogger(Bank.class);
    private static final int BANK_ID = 12;
    private static final int BANK_PANE_ID = 89;
    private static final int BANK_CLOSE_ID = 104;

    private static final int[] BANK_NPCS = {56, 56, 494, 495, 496};
    private static final int[] BANK_OBJECTS = {2213, 25808};

    /**
     * Retrieves the bank widget
     *
     * @return bank
     */
    private static RSWidget getBankWidget() {
        return Widgets.getWidget(BANK_ID, BANK_PANE_ID);
    }

    /**
     * Checks whether the bank is open or not.
     *
     * @return return true if bank interface is up else false.
     */
    public static boolean isOpen() {
        return Widgets.getWidget(BANK_ID, BANK_CLOSE_ID) != null;
    }

    /**
     * Opens the nearest on screen bank, with priority for objects over npcs.
     *
     * @return true if bank interface is showing.
     */
    public static boolean open() {
        if (isOpen()) {
            logger.debug("Bank is already open..");
            return true;
        }

        logger.info("Finding bank booth");
        Interactable bank = Objects.get(ObjectFilters.ID(BANK_OBJECTS));
        if (bank == null) {
            logger.debug("Couldn't find bank booth");
            bank = Npcs.get(NpcFilters.NAMED("Banker"));
            if (bank == null) {
                logger.debug("Couldn't find an object or NPC to bank at.");
                return false;
            }
        }
        logger.info("Clicking bank");
        bank.applyAction("Bank(.*)Bank");
        return isOpen();
    }

    /**
     * Closes the bank interface using the close widget.
     *
     * @return true if bank interface isn't showing.
     */
    public static boolean close() {
        if (!isOpen())
            return true;
        RSWidget close = Widgets.getWidget(BANK_ID, BANK_CLOSE_ID);
        if (close != null) {
            close.click(true);
            return true;
        }
        return false;
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
     * Retrieves all the items in the Bank
     *
     * @return an array containing all items in the Bank.
     */
    public static RSWidgetItem[] getAll() {
        RSWidget bank = getBankWidget();
        int[] items = bank.getInventoryItems();
        int[] stacks = bank.getInventoryStackSizes();
        List<RSWidgetItem> wrappers = new ArrayList<RSWidgetItem>();
        for (int i = 0; i < items.length; i++) {
            if (items[i] > 0 && stacks[i] > 0) {
                int col = (i % 8);
                int row = (i / 8);
                int x = bank.getX() + (col * 47) + 20;
                int y = bank.getY() + (row * 38) + 18;
                Rectangle area =  new Rectangle(x - (46 / 2), y - (36 / 2), 36, 32);
                RSWidgetItem item = new RSWidgetItem(area, items[i] - 1, stacks[i]);
                wrappers.add(item);
            }
        }
        return wrappers.toArray(new RSWidgetItem[wrappers.size()]);
    }

    /**
     * Checks if the Bank contains a specific item
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
     * Checks if the Bank contains at least the specified amount of the
     * specified item,
     *
     * @param id     RSWidgetItem to count
     * @param amount Minimum amount to pass.
     * @return true if the Bank contains at least the amount specified.
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
     * Checks if the Bank contains at most the specified amount of the specified
     * item,
     *
     * @param id     RSWidgetItem to count
     * @param amount Maximum amount to pass.
     * @return true if the Bank contains at most the amount specified.
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
     * Checks if the Bank contains at least one of the specified items
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
     * Checks if the Bank contains all of the specified items
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
     * @param id RSWidgetItem ID of the items to count
     * @return total amount of items matching id in Bank.
     */
    public static int count(int id) {
        int count = 0;
        for (RSWidgetItem item : getAll()) {
            if (item.getId() == id) {
                count += item.getStackSize();
            }
        }
        return count;
    }

    /**
     * Deposits all items of which the id matches any of the IDs specified.
     *
     * @param ids set of IDs to deposit.
     */
    public static void depositAll(int... ids) {
        RSWidgetItem item = Inventory.get(ids);
        do {
            if (item != null && item.applyAction("Store All")) {
                sleepNoException(500, 1000);
            }
        } while ((item = Inventory.get(ids)) != null);
    }

    /**
     * Deposits all items of which the id matches any of the IDs specified.
     *
     * @param ids set of IDs to deposit.
     */
    public static void depositAllExcept(final int... ids) {
        Predicate<RSWidgetItem> not = new Predicate<RSWidgetItem>() {
            @Override
            public boolean apply(RSWidgetItem object) {
                boolean match = false;
                for (int id : ids) {
                    if (object.getId() == id) match = true;
                }
                return !match;
            }
        };

        RSWidgetItem item = Inventory.get(not);
        do {
            if (item != null && item.applyAction("Store All")) {
                sleepNoException(500, 1000);
            }
        } while ((item = Inventory.get(not)) != null);
    }

    /**
     * Deposits a single item
     *
     * @param id ID of the item to deposit.
     */
    public static void deposit(int id) {
        RSWidgetItem item = Inventory.get(id);
        if (item != null) {
            item.click(true);
        }
    }

}
