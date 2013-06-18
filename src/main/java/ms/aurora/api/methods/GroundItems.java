package ms.aurora.api.methods;

import ms.aurora.api.Context;
import ms.aurora.api.util.ArrayUtils;
import ms.aurora.api.util.Predicate;
import ms.aurora.api.wrappers.Deque;
import ms.aurora.api.wrappers.GroundItem;
import ms.aurora.rt3.IClient;
import ms.aurora.rt3.IDeque;
import ms.aurora.rt3.IItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Ground item related functions
 *
 * @author tobiewarburton
 */
public final class GroundItems {

    /**
     * finds the closest {@link ms.aurora.api.wrappers.GroundItem} which matches the given predicate
     *
     * @param predicate the {@link Predicate} in which is required to be satisfied
     * @return the closest {@link ms.aurora.api.wrappers.GroundItem} which satisfies the predicate if there is one or null
     * @see ms.aurora.api.wrappers.GroundItem#distance(ms.aurora.api.wrappers.Locatable)
     * @see Predicate
     */
    public static GroundItem get(final Predicate<GroundItem>... predicate) {
        return getClosest(ArrayUtils.filter(getAll(), predicate).toArray(new GroundItem[0]));
    }

    /**
     * finds an array of {@link ms.aurora.api.wrappers.GroundItem} which match the given predicate
     *
     * @param predicate the {@link Predicate} in which is required to be satisfied
     * @return the array containing all {@link ms.aurora.api.wrappers.GroundItem} which satisfy the predicate
     * @see Predicate
     */
    public static GroundItem[] getAll(final Predicate<GroundItem>... predicate) {
        return ArrayUtils.filter(getAll(), predicate).toArray(new GroundItem[0]);
    }

    /**
     * finds an array containing all of the {@link ms.aurora.api.wrappers.GroundItem} in the current region
     *
     * @return an array containing all of the {@link ms.aurora.api.wrappers.GroundItem} in the current region
     */
    public static GroundItem[] getAll() {
        return _getAll().toArray(new GroundItem[0]);
    }

    /**
     * Gets the closest ground item out of an array of ground items
     *
     * @param objects array of ground items
     * @return closest ground item
     */
    private static GroundItem getClosest(GroundItem[] objects) {
        GroundItem closest = null;
        int closestDistance = 9999;
        for (GroundItem object : objects) {
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
    private static List<GroundItem> _getAll() {
        List<GroundItem> items = new ArrayList<GroundItem>();
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
    private static List<GroundItem> getItemsAt(int x, int y) {
        IClient client = Context.getClient();
        int z = client.getPlane();
        IDeque _deque = client.getGroundItems()[z][x][y];
        List<GroundItem> items = new ArrayList<GroundItem>();
        if (_deque != null) {
            Deque deque = new Deque(_deque);
            while (deque.hasNext()) {
                IItem item = (IItem) deque.next();
                if (item != null)
                    items.add(new GroundItem(item, x, y, z));
            }
        }
        return items;
    }
}
