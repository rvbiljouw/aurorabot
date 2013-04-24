package ms.aurora.api.methods.filters;

import ms.aurora.api.util.Predicate;
import ms.aurora.api.wrappers.RSObject;
import ms.aurora.api.wrappers.RSTile;

/**
 * @author tobiewarburton
 */
public final class ObjectFilters {

    private ObjectFilters() {
    }

    /**
     * a predicate which tests if the ID of the {@link ms.aurora.api.wrappers.RSObject} matches the specified.
     *
     * @param id the id of the {@link ms.aurora.api.wrappers.RSObject} you want to match
     * @return true the id of the {@link ms.aurora.api.wrappers.RSObject} matches the specified else false
     * @see ms.aurora.api.wrappers.RSObject#getId()
     */
    public static Predicate<RSObject> ID(final int id) {
        return new Predicate<RSObject>() {
            @Override
            public boolean apply(RSObject object) {
                return object.getId() == id;
            }
        };
    }

    /**
     * a predicate which tests if the ID of the {@link ms.aurora.api.wrappers.RSObject} matches the specified.
     *
     * @param ids the id array of the {@link ms.aurora.api.wrappers.RSObject} you want to match
     * @return true the id of the {@link ms.aurora.api.wrappers.RSObject} matches the specified else false
     * @see ms.aurora.api.wrappers.RSObject#getId()
     */
    public static Predicate<RSObject> ID(final int... ids) {
        return new Predicate<RSObject>() {
            @Override
            public boolean apply(RSObject object) {
                for (int id : ids) {
                    if (object.getId() == id) {
                        return true;
                    }
                }
                return false;
            }
        };
    }

    /**
     * a predictae which tests the location of the {@link ms.aurora.api.wrappers.RSObject} matches specified.
     *
     * @param location location that the {@link ms.aurora.api.wrappers.RSObject} should be on.
     * @return true if the location matches the {@link ms.aurora.api.wrappers.RSObject} location.
     */
    public static Predicate<RSObject> LOCATION(final RSTile location) {
        return new Predicate<RSObject>() {
            @Override
            public boolean apply(RSObject object) {
                return object.getLocation().equals(location);
            }
        };
    }

}
