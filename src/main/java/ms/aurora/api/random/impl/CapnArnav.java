package ms.aurora.api.random.impl;

import ms.aurora.api.methods.filters.NpcFilters;
import ms.aurora.api.methods.filters.ObjectFilters;
import ms.aurora.api.random.Random;
import ms.aurora.api.util.Utilities;
import ms.aurora.api.wrappers.RSNPC;

/**
 * @author tobiewarburton
 */
public class CapnArnav extends Random {
    private static final int CAPTAIN_ID = 2308;
    private static final int PORTAL_ID = 11369;

    private static class Interfaces {
        public static final int PARENT = 185;
        public static final int FIRST_SET_UP = 3;
        public static final int FIRST_SET_DOWN = 2;
        public static final int SECOND_SET_UP = 10;
        public static final int SECOND_SET_DOWN = 9;
        public static final int THIRD_SET_UP = 17;
        public static final int THIRD_SET_DOWN = 16;
        public static final int CONFIRM_BUTTON = 28;
    }

    @Override
    public boolean activate() {
        return npcs.get(NpcFilters.ID(CAPTAIN_ID)) != null && objects.get(ObjectFilters.ID(PORTAL_ID)) != null;
    }

    @Override
    public int loop() {
        RSNPC captain = npcs.get(NpcFilters.ID(CAPTAIN_ID));
        bank.close();
        if (!activate()) return -1;
        if (players.getLocal().isMoving() || (players.getLocal().getAnimation() != -1)) {
            Utilities.sleepNoException(500, 1000);
        }


        return 0;
    }
}
