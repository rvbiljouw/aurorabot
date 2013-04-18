package ms.aurora.api.methods.tabs;

import ms.aurora.api.Context;
import ms.aurora.api.methods.*;
import ms.aurora.api.util.Predicate;
import ms.aurora.api.util.Utilities;
import ms.aurora.api.wrappers.Interactable;
import ms.aurora.api.wrappers.RSWidget;
import ms.aurora.input.VirtualMouse;
import ms.aurora.rt3.Mouse;
import org.apache.log4j.Logger;

import java.awt.*;
import java.util.List;

import static com.google.common.collect.Collections2.filter;
import static com.google.common.collect.Lists.newArrayList;
import static ms.aurora.api.util.Utilities.sleepNoException;

/**
 * Date: 18/04/13
 * Time: 12:47
 *
 * @author A_C/Cov
 */
public class Shop {
    private static final Logger logger = Logger.getLogger(Shop.class);

    private static final int SHOP_WIDGET_GROUP_ID = 300;
    private static final int SHOP_ITEMS_WIDGET_ID = 75;
    private static final int SHOP_CLOSE_WIDGET_ID = 92;

    public enum Amount {
        ONE, FIVE, TEN, ALL
    }

    private static RSWidget getShopWidget() {
        return Widgets.getWidget(SHOP_WIDGET_GROUP_ID, SHOP_ITEMS_WIDGET_ID);
    }

    public static boolean isOpen() {
        return Widgets.getWidget(SHOP_WIDGET_GROUP_ID, SHOP_CLOSE_WIDGET_ID) != null;
    }

    public static boolean close() {
        if (!isOpen())
            return true;
        RSWidget close = Widgets.getWidget(SHOP_WIDGET_GROUP_ID, SHOP_CLOSE_WIDGET_ID);
        if (close != null) {
            close.click(true);
            return true;
        }
        return false;
    }

    /**
     * Gets all the items in the shop.
     * @return array containing all the items in the shop.
     */
    public static ShopItem[] getAll() {
        List<ShopItem> items = newArrayList();
        if (isOpen()) {
            RSWidget itemPane = getShopWidget();
            int[] ids = itemPane.getInventoryItems();
            int[] stacks = itemPane.getInventoryStackSizes();
            for (int i = 0; i < ids.length; i++) {
                if (ids[i] - 1 > 0 && stacks[i] > 0) {
                    ShopItem item = new ShopItem(itemPane, ids[i], stacks[i]);
                    item.slot = i;
                    items.add(item);
                }
            }
        }
        return items.toArray(new ShopItem[items.size()]);
    }

    /**
     * Retrieves all items that match the supplied predicate.
     * @param predicate Predicate to match items against.
     * @return An array of all matching items (can be empty).
     */
    public static ShopItem[] getAll(final Predicate<ShopItem> predicate) {
        return filter(newArrayList(getAll()),
                new com.google.common.base.Predicate<ShopItem>() {
                    @Override
                    public boolean apply(ShopItem item) {
                        return predicate.apply(item);
                    }
                }).toArray(new ShopItem[0]);
    }

    /**
     * Gets the first item in the shop with the corresponding id.
     * @param id id of item to get.
     * @return item with the corresponding id else null.
     */
    public static ShopItem get(int id) {
        for (ShopItem item: getAll()) {
            if (item.getId() == id) {
                return item;
            }
        }
        return null;
    }

    /**
     * Retrieves the first item that matches the predicate.
     * @param predicate Predicate to match items against
     * @return the first matching item, or null if none were found.
     */
    public static ShopItem get(final Predicate<ShopItem> predicate) {
        ShopItem[] items = getAll(predicate);
        if (items.length > 0) {
            return items[0];
        }
        return null;
    }

    /**
     * Will buy the amount of item with the corresponding id
     * @param id id of item to buy.
     * @param amount amount to buy.
     */
    public static void buy(int id, Amount amount) {
        ShopItem item = get(id);
        if (item == null || !isOpen()) {
            return;
        }
        switch (amount) {
            case ONE:
                item.click(true);
                sleepNoException(500, 750);
                break;
            case FIVE:
                item.applyAction("Buy 5");
                sleepNoException(500, 750);
                break;
            case TEN:
                item.applyAction("Buy 10");
                sleepNoException(500, 750);
                break;
            case ALL:
                do {
                    item.applyAction("Buy 10");
                    sleepNoException(200);
                } while (!Inventory.isFull());
                break;
        }
    }

    public static class ShopItem implements Interactable {

        private int id, stackSize, slot;
        private RSWidget container;

        private ShopItem(RSWidget container, int id, int stackSize) {
            this.container = container;
            this.id = id;
            this.stackSize = stackSize;
        }

        public int getStackSize() {
            return stackSize;
        }

        public int getId() {
            return id;
        }

        public Rectangle getArea() { // TODO: will only work for the first row?
            // until we getScrollBarShit
            int col = (slot % 8);
            int row = (slot / 8);
            int x = container.getX() + (col * 47) + 22;
            int y = container.getY() + (row * 47) + 18;
            return new Rectangle(x - (46 / 2), y - (36 / 2), 32, 32);
        }

        @Override
        public boolean applyAction(String action) {
            Rectangle area = getArea();
            VirtualMouse.moveMouse((int) area.getCenterX(), (int) area.getCenterY());
            return ms.aurora.api.methods.Menu.click(action);
        }

        @Override
        public boolean hover() {
            Rectangle area = getArea();
            VirtualMouse.moveMouse((int) area.getCenterX(), (int) area.getCenterY());
            Mouse clientMouse = Context.getClient().getMouse();
            return area.contains(clientMouse.getRealX(), clientMouse.getRealX());
        }

        @Override
        public boolean click(boolean left) {
            Rectangle area = getArea();
            VirtualMouse.clickMouse((int) area.getCenterX(), (int) area.getCenterY(), left);
            return true;
        }

    }

}
