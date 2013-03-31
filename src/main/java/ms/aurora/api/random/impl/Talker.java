package ms.aurora.api.random.impl;

import ms.aurora.api.random.Random;
import ms.aurora.api.util.Predicate;
import ms.aurora.api.util.Utilities;
import ms.aurora.api.wrappers.RSNPC;

import java.util.Arrays;
import java.util.List;

/**
 * @author tobiewarburton
 */
public class Talker extends Random {
    public static final List<String> TALKERS = Arrays.asList("Mysterious Old Man", "Drunken Dwarf", "Genie",
            "Security Guard", "Rick Turpentine", "Dr Jekyll", "Cap'n Hand");
    public static final Predicate<RSNPC> TALKERS_PREDICATE = new Predicate<RSNPC>() {
        @Override
        public boolean apply(RSNPC object) {
            return TALKERS.contains(object.getName());
        }
    };
    private RSNPC talker = null;

    @Override
    public boolean activate() {
        talker = npcs.get(TALKERS_PREDICATE);
        return talker != null && talker.getMessage().contains(players.getLocal().getName());
    }

    @Override
    public int loop() {
        if (talker != null) {
            talker.applyAction("Talk");
            while (players.getLocal().isMoving()) {
                Utilities.sleepNoException(100, 200);
            }
            Utilities.sleepNoException(600, 800);
            while (widgets.canContinue()) {
                widgets.clickContinue();
            }
        }
        return -1;
    }
}
