package ms.aurora.api.methods;

import com.google.common.collect.Collections2;
import ms.aurora.api.Context;
import ms.aurora.api.util.Predicate;
import ms.aurora.api.wrappers.RSDeque;
import ms.aurora.api.wrappers.RSGroundItem;
import ms.aurora.rt3.Client;
import ms.aurora.rt3.Deque;
import ms.aurora.rt3.Item;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

/**
 * Ground item related functions
 *
 * @author tobiewarburton
 */
public final class GroundItems {

    /**
     * finds the closest {@link RSGroundItem} which matches the given predicate
     *
     * @param predicate the {@link Predicate} in which is required to be satisfied
     * @return the closest {@link RSGroundItem} which satisfies the predicate if there is one or null
     * @see RSGroundItem#distance(ms.aurora.api.wrappers.Locatable)
     * @see Predicate
     */
    public static RSGroundItem get(final Predicate<RSGroundItem>... predicate) {
        return getClosest(Collections2.filter(_getAll(),
                new com.google.common.base.Predicate<RSGroundItem>() {
                    @Override
                    public boolean apply(RSGroundItem item) {
                        for(Predicate p : predicate) {
                            if(!p.apply(item)) return false;
                        }
                        return true;
                    }
                }
        ).toArray(new RSGroundItem[]{}));
    }

    /**
     * finds an array of {@link RSGroundItem} which match the given predicate
     *
     * @param predicate the {@link Predicate} in which is required to be satisfied
     * @return the array containing all {@link RSGroundItem} which satisfy the predicate
     * @see Predicate
     */
    public static RSGroundItem[] getAll(final Predicate<RSGroundItem>... predicate) {
        return Collections2.filter(_getAll(),
                new com.google.common.base.Predicate<RSGroundItem>() {
                    @Override
                    public boolean apply(RSGroundItem item) {
                        for(Predicate p : predicate) {
                            if(!p.apply(item)) return false;
                        }
                        return true;
                    }
                }
        ).toArray(new RSGroundItem[]{});
    }

    /**
     * finds an array containing all of the {@link RSGroundItem} in the current region
     *
     * @return an array containing all of the {@link RSGroundItem} in the current region
     */
    public static RSGroundItem[] getAll() {
        return _getAll().toArray(new RSGroundItem[0]);
    }

    /**
     * Gets the closest ground item out of an array of ground items
     *
     * @param objects array of ground items
     * @return closest ground item
     */
    private static RSGroundItem getClosest(RSGroundItem[] objects) {
        RSGroundItem closest = null;
        int closestDistance = 9999;
        for (RSGroundItem object : objects) {
            int distance = object.distance(Players.getLocal());
            if (distance < closestDistance) {
                closestDistance = distance;
                closest = object;
            }
        }
        return closest;
    }

    /**
     * Fetches a list of all ground items in the currently loaded region.
     *
     * @return list of ground items
     */
    private static List<RSGroundItem> _getAll() {
        List<RSGroundItem> items = newArrayList();
        for (int x = 0; x < 104; x++) {
            for (int y = 0; y < 104; y++) {
                items.addAll(getItemsAt(x, y));
            }
        }
        return items;
    }

    /**
     * Gets a list of all the ground items on a specific tile
     *
     * @param x shifted local X of the tile
     * @param y shifted local Y of the tile
     * @return list of items on tile
     */
    private static List<RSGroundItem> getItemsAt(int x, int y) {
        Client client = Context.get().getClient();
        int z = client.getPlane();
        Deque _deque = client.getGroundItems()[z][x][y];
        List<RSGroundItem> items = newArrayList();
        if (_deque != null) {
            RSDeque deque = new RSDeque(_deque);
            while (deque.hasNext()) {
                Item item = (Item) deque.next();
                if (item != null)
                    items.add(new RSGroundItem(Context.get(), item, x, y, z));
            }
        }
        return items;
    }
}
