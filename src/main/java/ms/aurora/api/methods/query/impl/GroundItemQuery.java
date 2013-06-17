package ms.aurora.api.methods.query.impl;

import ms.aurora.api.Context;
import ms.aurora.api.wrappers.Deque;
import ms.aurora.api.wrappers.GroundItem;
import ms.aurora.rt3.IClient;
import ms.aurora.rt3.IDeque;
import ms.aurora.rt3.IItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Date: 24/05/13
 * Time: 13:04
 *
 * @author A_C/Cov
 */
public final class GroundItemQuery extends LocatableQuery<GroundItem, GroundItemQuery> {

    @Override
    public GroundItem[] result() {
        List<GroundItem> rsGroundItems = getAll();
        rsGroundItems =  filterResults(rsGroundItems);
        Collections.sort(rsGroundItems, DISTANCE_COMPARATOR);
        return rsGroundItems.toArray(new GroundItem[rsGroundItems.size()]);
    }

    public GroundItemQuery id(final int... ids) {
        this.addConditional(new Conditional() {
            @Override
            protected boolean accept(GroundItem type) {
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

    private List<GroundItem> getAll() {
        List<GroundItem> items = new ArrayList<GroundItem>();
        for (int x = 0; x < 104; x++) {
            for (int y = 0; y < 104; y++) {
                items.addAll(getItemsAt(x, y));
            }
        }
        return items;
    }

    private List<GroundItem> getItemsAt(int x, int y) {
        IClient client = Context.getClient();
        int z = client.getPlane();
        IDeque deque = client.getGroundItems()[z][x][y];
        List<GroundItem> items = new ArrayList<GroundItem>();
        if (deque != null) {
            Deque rsDeque = new Deque(deque);
            while (rsDeque.hasNext()) {
                IItem item = (IItem) rsDeque.next();
                if (item != null)
                    items.add(new GroundItem(item, x, y, z));
            }
        }
        return items;
    }
}
