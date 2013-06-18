package ms.aurora.api.methods.filters;

import ms.aurora.api.util.Predicate;
import ms.aurora.api.wrappers.NPC;

/**
 * Several NPC related filters
 *
 * @author Rick
 * @author tobiewarburton
 */
public final class NpcFilters {

    private NpcFilters() {
    }

    /**
     * tests of the current {@link ms.aurora.api.wrappers.NPC} is not in combat
     *
     * @see ms.aurora.api.wrappers.NPC#isInCombat()
     */
    public static final Predicate<NPC> NOT_IN_COMBAT = new Predicate<NPC>() {
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
    public static final Predicate<NPC> IDLE = new Predicate<NPC>() {
        @Override
        public boolean apply(NPC object) {
            return object.getAnimation() == -1 && !object.isInCombat()
                    && !object.isMoving();
        }
    };

    /**
     * a predicate which tests if the ID of the {@link ms.aurora.api.wrappers.NPC} matches the specified.
     *
     * @param ids the id array of the {@link ms.aurora.api.wrappers.NPC} you want to match
     * @return true the id of the {@link ms.aurora.api.wrappers.NPC} matches the specified else false
     * @see ms.aurora.api.wrappers.NPC#getId()
     */
    public static Predicate<NPC> ID(final int... ids) {
        return new Predicate<NPC>() {
            @Override
            public boolean apply(NPC object) {
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
     * @param names the names of the {@link ms.aurora.api.wrappers.NPC} you want to match
     * @return true if the name of the {@link ms.aurora.api.wrappers.NPC} matches the specified name
     * @see ms.aurora.api.wrappers.NPC#getName()
     */
    public static Predicate<NPC> NAMED(final String... names) {
        return new Predicate<NPC>() {
            @Override
            public boolean apply(NPC object) {
                for (String name : names) {
                    if (object.getName().equalsIgnoreCase(name)) {
                        return true;
                    }
                }
                return false;
            }
        };
    }

}
