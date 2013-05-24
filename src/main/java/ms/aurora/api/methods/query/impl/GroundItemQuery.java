package ms.aurora.api.methods.query.impl;

import ms.aurora.api.Context;
import ms.aurora.api.methods.query.Sort;
import ms.aurora.api.wrappers.RSDeque;
import ms.aurora.api.wrappers.RSGroundItem;
import ms.aurora.rt3.Client;
import ms.aurora.rt3.Deque;
import ms.aurora.rt3.Item;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Date: 24/05/13
 * Time: 13:04
 *
 * @author A_C/Cov
 */
public class GroundItemQuery extends LocatableQuery<RSGroundItem, GroundItemQuery> {

    @Override
    public RSGroundItem[] result() {
        List<RSGroundItem> rsGroundItems = getAll();
        if (this.comparator == null) {
            switch (sortType) {
                case DISTANCE:
                    comparator = Sort.DISTANCE;
                    break;
                default:
                    comparator = Sort.DEFAULT;
            }
        }
        Collections.sort(filterResults(rsGroundItems), comparator);
        return rsGroundItems.toArray(new RSGroundItem[rsGroundItems.size()]);
    }

    public GroundItemQuery id(final int... ids) {
        this.addConditional(new Conditional() {
            @Override
            protected boolean accept(RSGroundItem type) {
                for (int id: ids) {
                    if (id == type.getId()) {
                        return true;
                    }
                }
                return false;
            }
        });
        return this;
    }

    private List<RSGroundItem> getAll() {
        List<RSGroundItem> items = new ArrayList<RSGroundItem>();
        for (int x = 0; x < 104; x++) {
            for (int y = 0; y < 104; y++) {
                items.addAll(getItemsAt(x, y));
            }
        }
        return items;
    }

    private List<RSGroundItem> getItemsAt(int x, int y) {
        Client client = Context.getClient();
        int z = client.getPlane();
        Deque deque = client.getGroundItems()[z][x][y];
        List<RSGroundItem> items = new ArrayList<RSGroundItem>();
        if (deque != null) {
            RSDeque rsDeque = new RSDeque(deque);
            while (rsDeque.hasNext()) {
                Item item = (Item) rsDeque.next();
                if (item != null)
                    items.add(new RSGroundItem(item, x, y, z));
            }
        }
        return items;
    }
}
