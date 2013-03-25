package ms.aurora.api.methods.filters;

import ms.aurora.api.util.Predicate;
import ms.aurora.api.wrappers.RSPlayer;

/**
 * @author tobiewarburton
 */
public final class PlayerFilters {

    private PlayerFilters() { }

    /**
     * tests of the current {@link ms.aurora.api.wrappers.RSPlayer} is not in combat
     *
     * @see ms.aurora.api.wrappers.RSPlayer#isInCombat()
     */
    public static final Predicate<RSPlayer> NOT_IN_COMBAT = new Predicate<RSPlayer>() {
        @Override
        public boolean apply(RSPlayer object) {
            return !object.isInCombat();
        }
    };
    /**
     * tests if the given {@link ms.aurora.api.wrappers.RSPlayer} is idle
     *
     * @see ms.aurora.api.wrappers.RSPlayer#getAnimation()
     * @see ms.aurora.api.wrappers.RSPlayer#isInCombat()
     * @see ms.aurora.api.wrappers.RSPlayer#isMoving()
     */
    public static final Predicate<RSPlayer> IDLE = new Predicate<RSPlayer>() {
        @Override
        public boolean apply(RSPlayer object) {
            return object.getAnimation() == -1 && !object.isInCombat()
                    && !object.isMoving();
        }
    };

    /**
     * @param name the name of the {@link RSPlayer} you want to match
     * @return true if the name of the {@link RSPlayer} matches the specified name
     * @see ms.aurora.api.wrappers.RSPlayer#getName()
     */
    public static Predicate<RSPlayer> NAMED(final String name) {
        return new Predicate<RSPlayer>() {
            @Override
            public boolean apply(RSPlayer object) {
                return object.getName().equals(name);
            }
        };
    }

}
