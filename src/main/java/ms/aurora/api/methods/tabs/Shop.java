package ms.aurora.api.methods.tabs;

import ms.aurora.api.methods.Widgets;
import ms.aurora.api.util.ArrayUtils;
import ms.aurora.api.util.Predicate;
import ms.aurora.api.util.StatePredicate;
import ms.aurora.api.wrappers.RSWidget;
import ms.aurora.api.wrappers.RSWidgetItem;
import org.apache.log4j.Logger;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

import static ms.aurora.api.util.Utilities.sleepUntil;

/**
 * Date: 18/04/13
 * Time: 12:47
 *
 * @author A_C/Cov
 */
public final class Shop {
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
     *
     * @return array containing all the items in the shop.
     */
    public static RSWidgetItem[] getAll() {
        List<RSWidgetItem> items = new ArrayList<RSWidgetItem>();
        if (isOpen()) {
            RSWidget itemPane = getShopWidget();
            int[] ids = itemPane.getInventoryItems();
            int[] stacks = itemPane.getInventoryStackSizes();
            for (int i = 0; i < ids.length; i++) {
                if (ids[i] - 1 > 0 && stacks[i] > 0) {
                    int col = (i % 8);
                    int row = (i / 8);
                    int x = itemPane.getX() + (col * 47) + 22;
                    int y = itemPane.getY() + (row * 47) + 18;
                    Rectangle area = new Rectangle(x - (46 / 2), y - (36 / 2), 32, 32);
                    RSWidgetItem item = new RSWidgetItem(area, ids[i] - 1, stacks[i]);
                    items.add(item);
                }
            }
        }
        return items.toArray(new RSWidgetItem[items.size()]);
    }

    /**
     * Retrieves all items that match the supplied predicate.
     *
     * @param predicate Predicate to match items against.
     * @return An array of all matching items (can be empty).
     */
    public static RSWidgetItem[] getAll(final Predicate<RSWidgetItem>... predicate) {
        List<RSWidgetItem> filter = ArrayUtils.filter(getAll(), predicate);
        return filter.toArray(new RSWidgetItem[filter.size()]);
    }

    /**
     * Retrieves the first item that matches the predicate.
     *
     * @param predicate Predicate to match items against
     * @return the first matching item, or null if none were found.
     */
    public static RSWidgetItem get(final Predicate<RSWidgetItem>... predicate) {
        RSWidgetItem[] items = getAll(predicate);
        if (items.length > 0) {
            return items[0];
        }
        return null;
    }

    /**
     * Will buy the amount of item with the corresponding id
     *
     * @param predicate predicate of item to buy.
     * @param amount amount to buy.
     */
    public static void buy(Predicate<RSWidgetItem> predicate, Amount amount) {
        RSWidgetItem item = get(predicate);
        if (item == null || !isOpen()) {
            return;
        }
        final int invSize = Inventory.getCount();
        StatePredicate invChange = new StatePredicate() {
            @Override
            public boolean apply() {
                return invSize < Inventory.getCount();
            }
        };
        switch (amount) {
            case ONE:
                if (item.applyAction("Buy 1")) {
                    sleepUntil(invChange, 2000);
                }
                break;
            case FIVE:
                if (item.applyAction("Buy 5")) {
                    sleepUntil(invChange, 2000);
                }
                break;
            case TEN:
                if (item.applyAction("Buy 10")) {
                    sleepUntil(invChange, 2000);
                }
                break;
            case ALL:
                while (!Inventory.isFull()) {
                    if (item.applyAction("Buy 10")) {
                        sleepUntil(invChange, 2000);
                    }
                }
                break;
        }
    }

}
