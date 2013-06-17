package ms.aurora.api.methods.filters;

import ms.aurora.api.util.Predicate;
import ms.aurora.api.wrappers.GameObject;
import ms.aurora.api.wrappers.NPC;
import ms.aurora.api.wrappers.Tile;

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
    public static Predicate<GameObject> OBJECT_ID(final int... ids) {
        return new Predicate<GameObject>() {
            @Override
            public boolean apply(GameObject object) {
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
    public static Predicate<NPC> NPC_ID(final int... ids) {
        return new Predicate<NPC>() {
            @Override
            public boolean apply(NPC npc) {
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
    public static Predicate<NPC> NPC_NAME(final String... names) {
        return new Predicate<NPC>() {
            @Override
            public boolean apply(NPC npc) {
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
     * a predicate which tests the location of the {@link ms.aurora.api.wrappers.GameObject} matches specified.
     *
     * @param location location that the {@link ms.aurora.api.wrappers.GameObject} should be on.
     * @return true if the location matches the {@link ms.aurora.api.wrappers.GameObject} location.
     */
    public static Predicate<GameObject> OBJECT_LOCATION(final Tile location) {
        return new Predicate<GameObject>() {
            @Override
            public boolean apply(GameObject object) {
                return object.getLocation().equals(location);
            }
        };
    }


}
