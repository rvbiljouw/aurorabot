package ms.aurora.api;

import com.google.common.collect.Collections2;
import ms.aurora.api.rt3.*;
import ms.aurora.api.util.Predicate;
import ms.aurora.api.wrappers.RSDeque;
import ms.aurora.api.wrappers.RSGroundItem;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static ms.aurora.api.ClientContext.context;

/**
 * @author tobiewarburton
 */
public class GroundItems {
    public static RSGroundItem get(final Predicate<RSGroundItem> predicate) {
        return getClosest(Collections2.filter(_getAll(),
                new com.google.common.base.Predicate<RSGroundItem>() {
                    @Override
                    public boolean apply(RSGroundItem RSGroundItem) {
                        return predicate.apply(RSGroundItem);
                    }
                }
        ).toArray(new RSGroundItem[0]));
    }

    public static RSGroundItem[] getAll(final Predicate<RSGroundItem> predicate) {
        return Collections2.filter(_getAll(),
                new com.google.common.base.Predicate<RSGroundItem>() {
                    @Override
                    public boolean apply(RSGroundItem RSGroundItem) {
                        return predicate.apply(RSGroundItem);
                    }
                }
        ).toArray(new RSGroundItem[]{});
    }

    public static RSGroundItem[] getAll() {
        return _getAll().toArray(new RSGroundItem[0]);
    }

    private static RSGroundItem getClosest(RSGroundItem[] objects) {
        RSGroundItem closest = null;
        int closestDistance = 9999;
        for (RSGroundItem object : objects) {
            int distance = object.distance(context.get().getMyPlayer());
            if (distance < closestDistance) {
                closestDistance = distance;
                closest = object;
            }
        }
        return closest;
    }


    private static List<RSGroundItem> _getAll() {
        List<RSGroundItem> objects = newArrayList();
        for (int x = 0; x < 104; x++) {
            for (int y = 0; y < 104; y++) {
                objects.addAll(getItemsAt(x, y));
            }
        }
        return objects;
    }

    private static List<RSGroundItem> getItemsAt(int x, int y) {
        Client client = context.get().getClient();
        int z = client.getPlane();
        Deque _deque = client.getGroundItems()[z][x][y];
        List<RSGroundItem> items = newArrayList();
        if (_deque != null) {
            RSDeque deque = new RSDeque(_deque);
            while (deque.hasNext()) {
                Item item = (Item) deque.next();
                if (item != null)
                    items.add(new RSGroundItem(context.get(), item, x, y, z));
            }
        }
        return items;
    }
}
