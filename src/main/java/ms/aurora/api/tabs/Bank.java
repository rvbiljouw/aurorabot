package ms.aurora.api.tabs;

import com.sun.istack.internal.Nullable;
import ms.aurora.api.ClientContext;
import ms.aurora.api.Widgets;
import ms.aurora.api.util.Predicate;
import ms.aurora.api.wrappers.RSWidget;
import ms.aurora.input.VirtualMouse;

import java.awt.*;
import java.util.List;

import static com.google.common.collect.Collections2.filter;
import static com.google.common.collect.Lists.newArrayList;

/**
 * @author tobiewarburton
 */
public class Bank {
    private static final int BANK_ID = 12;
    private static final int BANK_PANE_ID = 89;
    private static final int BANK_CLOSE_ID = 102;

    /**
     * Retrieves the bank widget
     *
     * @return bank
     */
    private static RSWidget getBankWidget() {
        return Widgets.getWidget(BANK_ID, BANK_PANE_ID);
    }

    public static boolean isOpen() {
        return getBankWidget() != null;
    }

    public static boolean close() {
        if (!isOpen()) return true;
        RSWidget close = Widgets.getWidget(BANK_ID, BANK_CLOSE_ID);
        if (close != null) {
            close.click();
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
    public static BankItem get(final Predicate<BankItem> predicate) {
        BankItem[] items = getAll(predicate);
        if (items.length > 0) {
            return items[0];
        }
        return null;
    }

    /**
     * Retrieves the first item that matches the specified ID.
     *
     * @param id BankItem ID to look for
     * @return item if it was found, otherwise null.
     */
    public static BankItem get(int id) {
        for (BankItem item : getAll()) {
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
    public static BankItem[] getAll(final Predicate<BankItem> predicate) {
        return filter(newArrayList(getAll()),
                new com.google.common.base.Predicate<BankItem>() {
                    @Override
                    public boolean apply(@Nullable BankItem item) {
                        return predicate.apply(item);
                    }
                }
        ).toArray(new BankItem[0]);
    }

    /**
     * Retrieves all items that match the specified ID.
     *
     * @param id BankItem ID to look for
     * @return list of items found, which can be empty.
     */
    public static BankItem[] getAll(int id) {
        List<BankItem> items = newArrayList();
        for (BankItem item : getAll()) {
            if (item.getId() == id) {
                items.add(item);
            }
        }
        return items.toArray(new BankItem[items.size()]);
    }

    /**
     * Retrieves all the items in the Bank
     *
     * @return an array containing all items in the Bank.
     */
    public static BankItem[] getAll() {
        RSWidget bank = getBankWidget();
        int[] items = bank.getInventoryItems();
        int[] stacks = bank.getInventoryStackSizes();
        List<BankItem> wrappers = newArrayList();

        for (int i = 0; i < items.length; i++) {
            if (items[i] > 0 && stacks[i] > 0) {
                BankItem item = new BankItem(bank, items[i] - 1, stacks[i]);
                item.slot = i;
                wrappers.add(item);
            }
        }
        return wrappers.toArray(new BankItem[wrappers.size()]);
    }

    /**
     * Checks if the Bank contains a specific item
     *
     * @param id BankItem to look for
     * @return true if found, otherwise false.
     */
    public static boolean contains(int id) {
        for (BankItem item : getAll()) {
            if (item.getId() == id) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if the Bank contains at least the
     * specified amount of the specified item,
     *
     * @param id     BankItem to count
     * @param amount Minimum amount to pass.
     * @return true if the Bank contains at least the amount specified.
     */
    public static boolean containsMinimum(int id, int amount) {
        for (BankItem item : getAll()) {
            if (item.getId() == id && item.getStackSize() >= amount) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if the Bank contains at most the
     * specified amount of the specified item,
     *
     * @param id     BankItem to count
     * @param amount Maximum amount to pass.
     * @return true if the Bank contains at most the amount specified.
     */
    public static boolean containsMaximum(int id, int amount) {
        for (BankItem item : getAll()) {
            if (item.getId() == id && item.getStackSize() <= amount) {
                return true;
            }
        }
        return false;
    }

    /**
     * Checks if the Bank contains at least one
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
     * @param id BankItem ID of the items to count
     * @return total amount of items matching id in Bank.
     */
    public static int count(int id) {
        int count = 0;
        for (BankItem item : getAll()) {
            if (item.getId() == id) {
                count += item.getStackSize();
            }
        }
        return count;
    }


    /**
     * A class encapsulating Bank items.
     */
    public static class BankItem {
        protected int slot;
        private int id;
        private int stackSize;
        private RSWidget container;

        public BankItem(RSWidget container, int id) {
            this(container, id, 1);
        }

        public BankItem(RSWidget container, int id, int stackSize) {
            this.container = container;
            this.id = id;
            this.stackSize = stackSize;
        }

        public int getId() {
            return id;
        }

        public int getStackSize() {
            return stackSize;
        }

        public Rectangle getArea() { //TODO: will only work for the first row? until we getScrollBarShit
            int col = (slot % 8);
            int row = (slot / 8);
            int x = container.getX() + (col * 47) + 50;
            int y = container.getY() + (row * 37) + 50;

            return new Rectangle(x - (46 / 2), y - (36 / 2), 36, 32);
        }

        public void applyAction(String action) {
            VirtualMouse mouse = ClientContext.get().input.getMouse();
            Rectangle area = getArea();
            mouse.moveMouse((int) area.getCenterX(), (int) area.getCenterY());
            ms.aurora.api.Menu.click(action);
        }

        public void click() {
            VirtualMouse mouse = ClientContext.get().input.getMouse();
            Rectangle area = getArea();
            mouse.moveMouse((int) area.getCenterX(), (int) area.getCenterY());
            mouse.clickMouse(true);
        }
    }
}
