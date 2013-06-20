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
     * tests of the current {@link ms.aurora.api.wrappers.NPC} is not in combat
     *
     * @see ms.aurora.api.wrappers.NPC#isInCombat()
     */
    public static final Predicate<NPC> NPC_NOT_IN_COMBAT = new Predicate<NPC>() {
        @Override
        public boolean apply(NPC object) {
            return !object.isInCombat();
        }
    };

    /**
     * tests if the given {@link ms.aurora.api.wrappers.NPC} is idle
     *
     * @see ms.aurora.api.wrappers.NPC#getAnimation()
     * @see ms.aurora.api.wrappers.NPC#isInCombat()
     * @see ms.aurora.api.wrappers.NPC#isMoving()
     */
    public static final Predicate<NPC> NPC_IDLE = new Predicate<NPC>() {
        @Override
        public boolean apply(NPC object) {
            return object.getAnimation() == -1 && !object.isInCombat()
                    && !object.isMoving();
        }
    };

    /**
     * Creates a predicate to filter GroundItem's by checking if it's ID matches any of the ones specified.
     *
     * @param ids A list of IDs that are accepted by the filter.
     * @return predicate
     */
    public static Predicate<GroundItem> GROUNDITEM_ID(final int... ids) {
        return new Predicate<GroundItem>() {
            @Override
            public boolean apply(GroundItem object) {
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
     * tests of the current {@link ms.aurora.api.wrappers.Player} is not in combat
     *
     * @see ms.aurora.api.wrappers.Player#isInCombat()
     */
    public static final Predicate<Player> PLAYER_NOT_IN_COMBAT = new Predicate<Player>() {
        @Override
        public boolean apply(Player object) {
            return !object.isInCombat();
        }
    };
    /**
     * tests if the given {@link ms.aurora.api.wrappers.Player} is idle
     *
     * @see ms.aurora.api.wrappers.Player#getAnimation()
     * @see ms.aurora.api.wrappers.Player#isInCombat()
     * @see ms.aurora.api.wrappers.Player#isMoving()
     */
    public static final Predicate<Player> PLAYER_IDLE = new Predicate<Player>() {
        @Override
        public boolean apply(Player object) {
            return object.getAnimation() == -1 && !object.isInCombat()
                    && !object.isMoving();
        }
    };

    /**
     * @param name the name of the {@link ms.aurora.api.wrappers.Player} you want to match
     * @return true if the name of the {@link ms.aurora.api.wrappers.Player} matches the specified name
     * @see ms.aurora.api.wrappers.Player#getName()
     */
    public static Predicate<Player> PLAYER_NAME(final String name) {
        return new Predicate<Player>() {
            @Override
            public boolean apply(Player object) {
                return object.getName().equals(name);
            }
        };
    }

    /**
     * a predicate helper which creates a predicate for a widget item which
     * is for one of the specified ids
     *
     * @param ids the id's in which you want the widget item to be
     * @return a predicate which filters by the specified id's
     * @see ms.aurora.api.wrappers.WidgetItem#getId()
     */
    public static Predicate<WidgetItem> WIDGETITEM_ID(final int... ids) {
        return new Predicate<WidgetItem>() {
            @Override
            public boolean apply(WidgetItem object) {
                for (int id : ids) {
                    if (object.getId() == id) {
                        return true;
                    }
                }
                return false;
            }
        };
    }

    public static Predicate<World> WORLD_ID(final int worldNumber) {
        return new Predicate<World>() {
            @Override
            public boolean apply(World object) {
                return object.getWorldNo() == worldNumber;
            }
        };
    }

    public static Predicate<World> WORLD_COUNTRY(final String countryName) {
        return new Predicate<World>() {
            @Override
            public boolean apply(World object) {
                return object.getCountry().toLowerCase().contains(countryName.toLowerCase());
            }
        };
    }

}
