package ms.aurora.api.methods.filters;

import ms.aurora.api.util.Predicate;
import ms.aurora.api.wrappers.GameObject;
import ms.aurora.api.wrappers.Tile;

/**
 * @author tobiewarburton
 */
public final class ObjectFilters {

    private ObjectFilters() {
    }

    /**
     * a predicate which tests if the ID of the {@link ms.aurora.api.wrappers.GameObject} matches the specified.
     *
     * @param ids the id array of the {@link ms.aurora.api.wrappers.GameObject} you want to match
     * @return true the id of the {@link ms.aurora.api.wrappers.GameObject} matches the specified else false
     * @see ms.aurora.api.wrappers.GameObject#getId()
     */
    public static Predicate<GameObject> ID(final int... ids) {
        return new Predicate<GameObject>() {
            @Override
            public boolean apply(GameObject object) {
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
     * a predicate which tests the location of the {@link ms.aurora.api.wrappers.GameObject} matches specified.
     *
     * @param location location that the {@link ms.aurora.api.wrappers.GameObject} should be on.
     * @return true if the location matches the {@link ms.aurora.api.wrappers.GameObject} location.
     */
    public static Predicate<GameObject> LOCATION(final Tile location) {
        return new Predicate<GameObject>() {
            @Override
            public boolean apply(GameObject object) {
                return object.getLocation().equals(location);
            }
        };
    }

}
