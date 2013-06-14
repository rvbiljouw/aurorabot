package ms.aurora.api.methods.filters;

import ms.aurora.api.util.Predicate;
import ms.aurora.api.wrappers.RSNPC;
import ms.aurora.api.wrappers.RSObject;
import ms.aurora.api.wrappers.RSTile;

/**
 * A class containing all kinds of filters
 *
 * @author rvbiljouw
 */
public final class Filters {

    /**
     * Creates a predicate that filters a set of objects by the
     * specified list of IDs, leaving only the matching
     * results.
     * @param ids A list of object IDs that are wanted
     * @return predicate
     */
    public static Predicate<RSObject> OBJECT_ID(final int... ids) {
        return new Predicate<RSObject>() {
            @Override
            public boolean apply(RSObject object) {
                for (int id : ids) {
                    if (id == object.getId()) {
                        return true;
                    }
                }
                return false;
            }
        };
    }

    /**
     * Creates a predicate that filters a set of npcs by the
     * specified list of IDs, leaving only the matching
     * results.
     * @param ids A list of npc IDs that are wanted
     * @return predicate
     */
    public static Predicate<RSNPC> NPC_ID(final int... ids) {
        return new Predicate<RSNPC>() {
            @Override
            public boolean apply(RSNPC npc) {
                for (int id : ids) {
                    if (id == npc.getId()) {
                        return true;
                    }
                }
                return false;
            }
        };
    }

    /**
     * Creates a predicate that filters a set of npcs by the
     * specified list of names, leaving only the matching
     * results.
     * @param names A list of npc names that are wanted
     * @return predicate
     */
    public static Predicate<RSNPC> NPC_NAME(final String... names) {
        return new Predicate<RSNPC>() {
            @Override
            public boolean apply(RSNPC npc) {
                for (String name : names) {
                    if (npc.getName().toLowerCase().contains(name.toLowerCase())) {
                        return true;
                    }
                }
                return false;
            }
        };
    }

    /**
     * a predicate which tests the location of the {@link ms.aurora.api.wrappers.RSObject} matches specified.
     *
     * @param location location that the {@link ms.aurora.api.wrappers.RSObject} should be on.
     * @return true if the location matches the {@link ms.aurora.api.wrappers.RSObject} location.
     */
    public static Predicate<RSObject> OBJECT_LOCATION(final RSTile location) {
        return new Predicate<RSObject>() {
            @Override
            public boolean apply(RSObject object) {
                return object.getLocation().equals(location);
            }
        };
    }


}
