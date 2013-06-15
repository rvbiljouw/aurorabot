package ms.aurora.api.methods.filters;

import ms.aurora.api.methods.web.model.World;
import ms.aurora.api.util.Predicate;
import ms.aurora.api.wrappers.*;

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
     * @param name the name of the {@link RSPlayer} you want to match
     * @return true if the name of the {@link RSPlayer} matches the specified name
     * @see ms.aurora.api.wrappers.RSPlayer#getName()
     */
    public static Predicate<RSPlayer> PLAYER_NAME(final String name) {
        return new Predicate<RSPlayer>() {
            @Override
            public boolean apply(RSPlayer object) {
                return object.getName().equals(name);
            }
        };
    }

    /**
     * tests of the current {@link ms.aurora.api.wrappers.RSCharacter] is not in combat
     *
     * @see ms.aurora.api.wrappers.RSNPC#isInCombat()
     */
    public static final Predicate<RSCharacter> NOT_IN_COMBAT = new Predicate<RSCharacter>() {
        @Override
        public boolean apply(RSCharacter object) {
            return !object.isInCombat();
        }
    };

    /**
     * tests if the given {@link ms.aurora.api.wrappers.RSCharacter} is idle
     *
     * @see ms.aurora.api.wrappers.RSCharacter#getAnimation()
     * @see ms.aurora.api.wrappers.RSCharacter#isInCombat()
     * @see ms.aurora.api.wrappers.RSCharacter#isMoving()
     */
    public static final Predicate<RSCharacter> IDLE = new Predicate<RSCharacter>() {
        @Override
        public boolean apply(RSCharacter object) {
            return object.getAnimation() == -1 && !object.isInCombat()
                    && !object.isMoving();
        }
    };

    /**
     * Filters a ground item out by checking if it's ID matches any of the ones specified.
     *
     * @param ids A list of IDs that are accepted by the filter.
     * @return predicate
     */
    public static Predicate<RSGroundItem> GROUND_ITEM_IDS(final int... ids) {
        return new Predicate<RSGroundItem>() {
            @Override
            public boolean apply(RSGroundItem object) {
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
     * Filters a world based on it's world number
     * @param worldNumber World number
     * @return predicate
     */
    public static Predicate<World> WORLD_ID(final int worldNumber) {
        return new Predicate<World>() {
            @Override
            public boolean apply(World object) {
                return object.getWorldNo() == worldNumber;
            }
        };
    }

    /**
     * Filters a world based on it's country name.
     * @param countryName Country name
     * @return predicate
     */
    public static Predicate<World> WORLD_COUNTRY(final String countryName) {
        return new Predicate<World>() {
            @Override
            public boolean apply(World object) {
                return object.getCountry().toLowerCase().contains(countryName.toLowerCase());
            }
        };
    }


}
