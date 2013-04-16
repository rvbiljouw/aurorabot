package ms.aurora.api.methods.filters;

import ms.aurora.api.util.Predicate;
import ms.aurora.api.wrappers.RSGroundItem;

/**
 * A class containing several filters for RSGroundItem
 *
 * @author rvbiljouw
 */
public final class GroundItemFilters {

    private GroundItemFilters() {

    }

    /**
     * Filters a ground item out by checking if it's ID matches any of the ones specified.
     * @param ids A list of IDs that are accepted by the filter.
     * @return predicate
     */
    public static Predicate<RSGroundItem> ID(final int... ids) {
        return new Predicate<RSGroundItem>() {
            @Override
            public boolean apply(RSGroundItem object) {
                for(int id : ids) {
                    if(object.getId() == id) {
                        return true;
                    }
                }
                return false;
            }
        };
    }
}
