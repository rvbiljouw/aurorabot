package ms.aurora.api.random.impl;

import ms.aurora.api.methods.*;
import ms.aurora.api.methods.filters.NpcFilters;
import ms.aurora.api.methods.filters.ObjectFilters;
import ms.aurora.api.random.Random;
import ms.aurora.api.wrappers.RSNPC;

import static ms.aurora.api.util.Utilities.random;
import static ms.aurora.api.util.Utilities.sleepNoException;

/**
 * Created with IntelliJ IDEA.
 * User: tobiewarburton
 * Date: 01/04/13
 * Time: 11:35
 * To change this template use File | Settings | File Templates.
 */
public class FrogCave extends Random {

    private RSNPC frog;
    private boolean talkedToHerald;
    private int tries;

    @Override
    public boolean activate() {
        if (Npcs.get(NpcFilters.NAMED("Frog Herald")) != null
                && Objects.get(ObjectFilters.ID(5917)) != null) {
            sleepNoException(2000, 3000);
            return (Npcs.get(NpcFilters.NAMED("Frog Herald")) != null) && (Objects.get(ObjectFilters.ID(5917)) != null);
        }
        return false;
    }

    @Override
    public int loop() {
        try {
            if (!activate()) {
                talkedToHerald = false;
                frog = null;
                tries = 0;
                return -1;
            }
            if (Widgets.canContinue()) {
                talkedToHerald = true;
                Widgets.clickContinue();
                return random(600, 800);
            }
            if (Players.getLocal().isMoving()) {
                return random(600, 800);
            }
            if (!talkedToHerald) {
                final RSNPC herald = Npcs.get(NpcFilters.NAMED("Frog Herald"));
                if (Calculations.distance(Players.getLocal().getLocation(), herald.getLocation()) < 5) {
                    herald.applyAction("Talk-to");
                    return random(500, 1000);
                } else {
                    Walking.clickOnMap(herald.getLocation());
                    return random(500, 700);
                }
            }
            if (frog == null) {
                frog = findFrog();
            }
            if (frog != null && frog.getLocation() != null) {
                if (Calculations.distance(Players.getLocal().getLocation(), frog.getLocation()) < 5) {
                    frog.applyAction("Talk-to Frog");
                    return random(900, 1000);
                } else {
                    Walking.clickOnMap(frog.getLocation());
                    return random(500, 700);
                }
            } else {
                tries++;
                if (tries > 200) {
                    tries = 0;
                    talkedToHerald = false;
                }
                return random(200, 400);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return random(200, 400);
    }

    private RSNPC findFrog() {
        for (RSNPC npc : Npcs.getAll()) {
            if (!npc.isMoving() && npc.getHeight() == -278) {
                return npc;
            }
        }
        return null;
    }
}
