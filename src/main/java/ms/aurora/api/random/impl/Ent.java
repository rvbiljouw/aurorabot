package ms.aurora.api.random.impl;

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
 * Solver for ents
 *
 * @author rvbiljouw
 */
@AfterLogin
@RandomManifest(name = "Ent", version = 1.0)
public class Ent extends Random {
    private static final String[] ENT_NAMES = {
        "Tree", "Willow", "Oak", "Yew", "Maple" // lolwot ?
    };


    @Override
    public boolean activate() {
        Entity interacting = Players.getLocal().getInteracting();
        if (interacting == null) {
            return false;
        }

        if (interacting instanceof NPC) {
            NPC npc = (NPC) interacting;
            for(String entName: ENT_NAMES) {
                if(npc.getName().toLowerCase().contains(entName.toLowerCase())) {
                    return true;
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
        Tile deviated = Tile.randomize(base, 4, 4);
        Walking.walkToLocal(deviated);
        return random(200, 300);
    }
}
