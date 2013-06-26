package ms.aurora.api.methods.tabs;

import ms.aurora.api.methods.*;
import ms.aurora.api.methods.filters.Filters;
import ms.aurora.api.util.ArrayUtils;
import ms.aurora.api.util.Predicate;
import ms.aurora.api.util.StatePredicate;
import ms.aurora.api.util.Utilities;
import ms.aurora.api.wrappers.*;
import org.apache.log4j.Logger;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static ms.aurora.api.methods.Calculations.distance;
import static ms.aurora.api.util.Utilities.sleepNoException;

/**
 * @author tobiewarburton
 */
public final class Bank {
    private static final Logger logger = Logger.getLogger(Bank.class);
    private static final int BANK_ID = 12;
    private static final int BANK_PANE_ID = 89;
    private static final int BANK_CLOSE_ID = 104;
    private static final int[] BANK_NPCS = {56, 494, 495, 496};
    private static final int[] BANK_OBJECTS = {782, 2012, 2015, 2213,
            4483, 2453, 6084, 11402, 11758, 12759, 14367, 19230, 24914, 25808,
            26972, 27663, 29085, 34752, 35647, 36786, 4483, 8981, 14382, 20607, 21301
    };


    private static final StatePredicate WALKING() {
        return new StatePredicate() {
            @Override
            public boolean apply() {
                return Players.getLocal().isMoving();
            }
        };
    }

    private static final StatePredicate WALKING(final Tile tile, final int distance) {
        return new StatePredicate() {
            @Override
            public boolean apply() {
                return Players.getLocal().isMoving() && distance(tile, Players.getLocal().getLocation()) > distance;
            }
        };
    }

    /**
     * Retrieves the bank widget
     *
     * @return bank
     */
    private static Widget getBankWidget() {
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

        Interactable bank = Objects.get(Filters.OBJECT_ID(BANK_OBJECTS));
        if (bank == null) {
            bank = Npcs.get(Filters.NPC_NAME("Banker"));
            if (bank == null) {
                return false;
            }
        }

        if (bank instanceof NPC && !((NPC) bank).isOnScreen()) {
            Walking.walkToLocal(((NPC) bank).getLocation());
        } else if (bank instanceof GameObject && !((GameObject) bank).isOnScreen()) {
            Walking.walkToLocal(((GameObject) bank).getLocation());
        }

        Camera.setAngle(((Locatable) bank).getLocation());
        bank.applyAction("Bank(.*)Bank");
        Utilities.sleepUntil(WALKING(), 600);
        if (Players.getLocal().isMoving()) {
            Utilities.sleepWhile(WALKING(), 7500);
        }
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
        Widget close = Widgets.getWidget(BANK_ID, BANK_CLOSE_ID);
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
        return ArrayUtils.filter(getAll(), predicate).toArray(new WidgetItem[0]);
    }

    /**
     * Retrieves all the items in the Bank
     *
     * @return an array containing all items in the Bank.
     */
    public static WidgetItem[] getAll() {
        Widget bank = getBankWidget();
        int[] items = bank.getInventoryItems();
        int[] stacks = bank.getInventoryStackSizes();
        List<WidgetItem> wrappers = new ArrayList<WidgetItem>();
        for (int i = 0; i < items.length; i++) {
            if (items[i] > 0 && stacks[i] > 0) {
                int col = (i % 8);
                int row = (i / 8);
                int x = bank.getX() + (col * 47) + 20;
                int y = bank.getY() + (row * 38) + 18;
                Rectangle area = new Rectangle(x - (46 / 2), y - (36 / 2), 36, 32);
                WidgetItem item = new WidgetItem(area, items[i] - 1, stacks[i]);
                wrappers.add(item);
            }
        }
        return wrappers.toArray(new WidgetItem[wrappers.size()]);
    }

    /**
     * Checks if the Bank contains a specific item
     *
     * @param predicates RSWidgetItem to look for
     * @return true if found, otherwise false.
     */
    public static boolean contains(Predicate<WidgetItem>... predicates) {
        return getAll(predicates).length > 0;
    }

    /**
     * Checks if the Bank contains all of the specified items
     *
     * @param predicates A var-args list of Predicates.
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
     * Counts all the items matching the specified predicates.
     *
     * @param predicates RSWidgetItem predicate of the items to count
     * @return total amount of items matching id in Bank.
     */
    public static int count(Predicate<WidgetItem>... predicates) {
        int count = 0;
        for (WidgetItem item : getAll(predicates)) {
            count += item.getStackSize();
        }
        return count;
    }

    /**
     * Deposits all items of which the id matches any of the IDs specified.
     *
     * @param ids set of IDs to deposit.
     */
    public static void depositAll(Predicate<WidgetItem>... predicates) {
        WidgetItem item = Inventory.get(predicates);
        if (item != null && item.applyAction("Store All")) {
            sleepNoException(500, 1000);
        }
        if (Inventory.get(predicates) != null) {
            depositAll(predicates);
        }
    }

    /**
     * Deposits all items of which the id matches any of the IDs specified.
     *
     * @param ids set of IDs to deposit.
     */
    public static void depositAllExcept(final Predicate<WidgetItem>... predicates) {
        Predicate<WidgetItem> not = new Predicate<WidgetItem>() {
            @Override
            public boolean apply(WidgetItem object) {
                boolean match = false;
                for (Predicate<WidgetItem> predicate : predicates) {
                    if (predicate.apply(object)) match = true;
                }
                return !match;
            }
        };

        WidgetItem item = Inventory.get(not);
        if (item != null && item.applyAction("Store All")) {
            sleepNoException(500, 1000);
        }
        if (Inventory.get(not) != null) {
            depositAllExcept(predicates);
        }
    }

    /**
     * Deposits a single item
     *
     * @param id ID of the item to deposit.
     */
    public static void deposit(Predicate<WidgetItem>... predicates) {
        WidgetItem item = Inventory.get(predicates);
        if (item != null) {
            item.click(true);
        }
    }

}
