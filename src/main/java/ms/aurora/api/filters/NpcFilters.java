package ms.aurora.api.filters;

import ms.aurora.api.util.Predicate;
import ms.aurora.api.wrappers.RSNPC;

/**
 * @author rvbiljouw
 */
public class NpcFilters {


    public static final Predicate<RSNPC> ID(final int id) {
        return new Predicate<RSNPC>() {
            @Override
            public boolean apply(RSNPC object) {
                return object.getId() == id;
            }
        };
    }

    public static final Predicate<RSNPC> NAMED(final String name) {
        return new Predicate<RSNPC>() {
            @Override
            public boolean apply(RSNPC object) {
                return object.getName().equals(name);
            }
        };
    }

    public static final Predicate<RSNPC> IDLE = new Predicate<RSNPC>() {
        @Override
        public boolean apply(RSNPC object) {
            return object.getAnimation() == -1 && !object.isInCombat()
                    && !object.isMoving();
        }
    };

    public static final Predicate<RSNPC> NOT_IN_COMBAT = new Predicate<RSNPC>() {
        @Override
        public boolean apply(RSNPC object) {
            return !object.isInCombat();
        }
    };

}
