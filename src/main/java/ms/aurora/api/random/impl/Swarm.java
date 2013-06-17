package ms.aurora.api.random.impl;

import ms.aurora.api.Context;
import ms.aurora.api.methods.Players;
import ms.aurora.api.methods.Walking;
import ms.aurora.api.random.AfterLogin;
import ms.aurora.api.random.Random;
import ms.aurora.api.random.RandomManifest;
import ms.aurora.api.wrappers.Entity;
import ms.aurora.api.wrappers.NPC;
import ms.aurora.api.wrappers.Tile;

import static ms.aurora.api.util.Utilities.random;

/**
 * @author rvbiljouw
 */
@RandomManifest(name = "Swarm", version = 1.0)
@AfterLogin
public class Swarm extends Random {
    @Override
    public boolean activate() {
        Entity interacting = Players.getLocal().getInteracting();
        if (interacting == null) {
            return false;
        }

        if (interacting instanceof NPC) {
            NPC npc = (NPC) interacting;
            if (npc.getName().toLowerCase().contains("swarm") || npc.getName().toLowerCase().contains("troll")) {
                String combatRandomProperty = Context.getProperty("combatRandomsDisabled");
                if (combatRandomProperty == null || combatRandomProperty.equals("false")) {
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

        Tile base = Players.getLocal().getLocation();
        Tile deviated = Tile.randomize(base, 40, 40);
        Walking.walkTo(deviated);
        return random(200, 300);
    }
}
