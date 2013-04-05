package ms.aurora.api.methods.filters;

import ms.aurora.api.util.Predicate;
import ms.aurora.api.wrappers.RSNPC;

/**
 * Several NPC related filters
 * @author Rick
 * @author tobiewarburton
 */
public final class NpcFilters {

    private NpcFilters() {
    }

    /**
     * tests of the current {@link ms.aurora.api.wrappers.RSNPC} is not in combat
     *
     * @see ms.aurora.api.wrappers.RSNPC#isInCombat()
     */
    public static final Predicate<RSNPC> NOT_IN_COMBAT = new Predicate<RSNPC>() {
        @Override
        public boolean apply(RSNPC object) {
            return !object.isInCombat();
        }
    };

    /**
     * tests if the given {@link ms.aurora.api.wrappers.RSNPC} is idle
     *
     * @see ms.aurora.api.wrappers.RSNPC#getAnimation()
     * @see ms.aurora.api.wrappers.RSNPC#isInCombat()
     * @see ms.aurora.api.wrappers.RSNPC#isMoving()
     */
    public static final Predicate<RSNPC> IDLE = new Predicate<RSNPC>() {
        @Override
        public boolean apply(RSNPC object) {
            return object.getAnimation() == -1 && !object.isInCombat()
                    && !object.isMoving();
        }
    };

    /**
     * a predicate which tests if the ID of the {@link RSNPC} matches the specified.
     *
     * @param id the id of the {@link RSNPC} you want to match
     * @return true the id of the {@link RSNPC} matches the specified else false
     * @see ms.aurora.api.wrappers.RSNPC#getId()
     */
    public static Predicate<RSNPC> ID(final int id) {
        return new Predicate<RSNPC>() {
            @Override
            public boolean apply(RSNPC object) {
                return object.getId() == id;
            }
        };
    }

    /**
     * a predicate which tests if the ID of the {@link RSNPC} matches the specified.
     *
     * @param ids the id array of the {@link RSNPC} you want to match
     * @return true the id of the {@link RSNPC} matches the specified else false
     * @see ms.aurora.api.wrappers.RSNPC#getId()
     */
    public static Predicate<RSNPC> ID(final int... ids) {
        return new Predicate<RSNPC>() {
            @Override
            public boolean apply(RSNPC object) {
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
     * @param names the names of the {@link RSNPC} you want to match
     * @return true if the name of the {@link RSNPC} matches the specified name
     * @see ms.aurora.api.wrappers.RSNPC#getName()
     */
    public static Predicate<RSNPC> NAMED(final String... names) {
        return new Predicate<RSNPC>() {
            @Override
            public boolean apply(RSNPC object) {
                for (String name : names) {
                    if (object.getName().equals(name)) {
                        return true;
                    }
                }
                return false;
            }
        };
    }

}
