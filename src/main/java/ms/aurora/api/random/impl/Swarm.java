package ms.aurora.api.random.impl;

import ms.aurora.api.Context;
import ms.aurora.api.methods.Players;
import ms.aurora.api.methods.Walking;
import ms.aurora.api.random.Random;
import ms.aurora.api.random.RandomManifest;
import ms.aurora.api.wrappers.RSCharacter;
import ms.aurora.api.wrappers.RSNPC;
import ms.aurora.api.wrappers.RSTile;

import static ms.aurora.api.util.Utilities.random;

/**
 * @author rvbiljouw
 */
@RandomManifest(name = "Swarm", version = 1.0)
public class Swarm extends Random {
    @Override
    public boolean activate() {
        RSCharacter interacting = Players.getLocal().getInteracting();
        if (interacting == null) {
            return false;
        }

        if (interacting instanceof RSNPC) {
            RSNPC npc = (RSNPC) interacting;
            if (npc.getName().toLowerCase().contains("swarm")) {
                String combatRandomProperty = Context.getProperty("combatRandomsEnabled");
                if (combatRandomProperty == null || combatRandomProperty.equals("true")) {
                    return Players.getLocal().isInCombat();
                }
            }
        }
        return false;
    }

    @Override
    public int loop() {
        if (!activate()) {
            return -1;
        }

        RSTile base = Players.getLocal().getLocation();
        RSTile deviated = RSTile.randomize(base, 40, 40);
        Walking.walkTo(deviated);
        return random(200, 300);
    }
}
