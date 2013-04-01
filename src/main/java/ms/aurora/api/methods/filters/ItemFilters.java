package ms.aurora.api.methods.filters;

import ms.aurora.api.util.Predicate;
import ms.aurora.api.wrappers.RSGroundItem;

/**
 * Created with IntelliJ IDEA.
 * User: tobiewarburton
 * Date: 01/04/13
 * Time: 11:17
 * To change this template use File | Settings | File Templates.
 */
public final class ItemFilters {

    private ItemFilters() {

    }

    public static Predicate<RSGroundItem> ID(final int id) {
        return new Predicate<RSGroundItem>() {
            @Override
            public boolean apply(RSGroundItem object) {
                return object.getId() == id;
            }
        };
    }
}
