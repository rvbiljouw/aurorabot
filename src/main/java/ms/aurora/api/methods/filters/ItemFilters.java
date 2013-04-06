package ms.aurora.api.methods.filters;

import ms.aurora.api.util.Predicate;
import ms.aurora.api.wrappers.RSGroundItem;

/**
 * A class containing several filters for RSGroundItem
 *
 * @author rvbiljouw
 */
public final class ItemFilters {

    private ItemFilters() {

    }

    /**
     * Filters a ground item out by it's ID.
     *
     * @param id ID to filter
     * @return predicate
     */
    public static Predicate<RSGroundItem> ID(final int id) {
        return new Predicate<RSGroundItem>() {
            @Override
            public boolean apply(RSGroundItem object) {
                return object.getId() == id;
            }
        };
    }
}
