package ms.aurora.api.random.impl;

import ms.aurora.api.Context;
import ms.aurora.api.methods.Calculations;
import ms.aurora.api.methods.Players;
import ms.aurora.api.random.Random;
import ms.aurora.api.wrappers.RSTile;

/**
 * @author tobiewarburton
 */
public class ScapeRuneIsland extends Random {
    public RSTile CENTER_TILE = new RSTile(3421, 4777);

    @Override
    public boolean activate() {
        if(!Context.isLoggedIn()) return false;

        return Calculations.distance(Players.getLocal().getLocation(), CENTER_TILE) < 50;
    }

    @Override
    public int loop() {
        return -1;
    }
}
