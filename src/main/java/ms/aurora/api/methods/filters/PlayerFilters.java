package ms.aurora.api.methods.filters;

import ms.aurora.api.util.Predicate;
import ms.aurora.api.wrappers.Player;

/**
 * @author tobiewarburton
 */
public final class PlayerFilters {

    private PlayerFilters() {
    }

    /**
     * tests of the current {@link ms.aurora.api.wrappers.Player} is not in combat
     *
     * @see ms.aurora.api.wrappers.Player#isInCombat()
     */
    public static final Predicate<Player> NOT_IN_COMBAT = new Predicate<Player>() {
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
    public static final Predicate<Player> IDLE = new Predicate<Player>() {
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
    public static Predicate<Player> NAMED(final String name) {
        return new Predicate<Player>() {
            @Override
            public boolean apply(Player object) {
                return object.getName().equals(name);
            }
        };
    }

}
