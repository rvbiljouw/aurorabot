package ms.aurora.api.random.impl;

import ms.aurora.api.methods.Npcs;
import ms.aurora.api.methods.Players;
import ms.aurora.api.methods.Widgets;
import ms.aurora.api.random.AfterLogin;
import ms.aurora.api.random.Random;
import ms.aurora.api.random.RandomManifest;
import ms.aurora.api.util.Predicate;
import ms.aurora.api.util.Utilities;
import ms.aurora.api.wrappers.NPC;

import java.util.Arrays;
import java.util.List;

/**
 * @author tobiewarburton
 */
@AfterLogin
@RandomManifest(name = "Talker", version = 1.0)
public class Talker extends Random {
    public static final List<String> TALKERS = Arrays.asList("Mysterious Old Man", "Drunken Dwarf", "Genie",
            "Security Guard", "Rick Turpentine", "Dr Jekyll", "Cap'n Hand");
    public static final Predicate<NPC> TALKERS_PREDICATE = new Predicate<NPC>() {
        @Override
        public boolean apply(NPC object) {
            return TALKERS.contains(object.getName());
        }
    };
    private NPC talker = null;

    @Override
    public boolean activate() {
        talker = Npcs.get(TALKERS_PREDICATE);
        return talker != null && talker.getMessage().contains(Players.getLocal().getName());
    }

    @Override
    public int loop() {
        if (talker != null) {
            talker.applyAction("Talk");
            while (Players.getLocal().isMoving()) {
                Utilities.sleepNoException(100, 200);
            }
            Utilities.sleepNoException(600, 800);
            while (Widgets.canContinue()) {
                Widgets.clickContinue();
            }
        }
        return -1;
    }
}
