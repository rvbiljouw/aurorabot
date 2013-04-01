package ms.aurora.api.random.impl;

import ms.aurora.api.methods.Npcs;
import ms.aurora.api.methods.Players;
import ms.aurora.api.random.Random;
import ms.aurora.api.util.Predicate;
import ms.aurora.api.wrappers.RSNPC;

/**
 * @author tobiewarburton
 */
public class StrangePlant extends Random {
    private static final int STRANGE_PLANT_ID = 407;
    private static final int DEAD_ANIM = 350;
    private static final int ALIVE_ANIM = 348;


    private final Predicate<RSNPC> STRANGE_PLANT_PREDICATE = new Predicate<RSNPC>() {
        @Override
        public boolean apply(RSNPC object) {
            return object.getId() == STRANGE_PLANT_ID && object.distance(Players.getLocal()) == 1;
        }
    };


    private RSNPC plant;

    @Override
    public boolean activate() {
        plant = Npcs.get(STRANGE_PLANT_PREDICATE);
        return plant != null;
    }

    @Override
    public int loop() {
        if (plant != null) {
            if (plant.getAnimation() == DEAD_ANIM || plant.getAnimation() == ALIVE_ANIM) {
                return 250;
            }
            if (plant.getAnimation() <= 0) {
                plant.applyAction("Pick");
                return 5000; //TODO: check this; feels really long, rsbot used rand(6100,11000)
            }
        }
        return -1;
    }
}
