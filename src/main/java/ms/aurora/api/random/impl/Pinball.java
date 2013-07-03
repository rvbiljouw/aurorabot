package ms.aurora.api.random.impl;

import ms.aurora.api.methods.Camera;
import ms.aurora.api.methods.Objects;
import ms.aurora.api.methods.Players;
import ms.aurora.api.methods.Widgets;
import ms.aurora.api.methods.Predicates;
import ms.aurora.api.random.AfterLogin;
import ms.aurora.api.random.Random;
import ms.aurora.api.random.RandomManifest;
import ms.aurora.api.wrappers.GameObject;

import static ms.aurora.api.util.Utilities.random;

/**
 * Solves the pinball random event
 *
 * @author rvbiljouw
 */
@AfterLogin
@RandomManifest(name = "Pinball", version = 1.0)
public class Pinball extends Random {
    private final int POST_VALID[] = {15000, 15002, 15004, 15006, 15008};
    private final int POST_INVALID[] = {15001, 15003, 15005, 15007, 15009};
    private static final int DOOR_ID = 15010;

    @Override
    public boolean activate() {
        return Objects.get(Predicates.OBJECT_ID(POST_INVALID)) != null;
    }

    @Override
    public int loop() {
        if (!idle()) return random(1000, 2000);
        GameObject exit = Objects.get(Predicates.OBJECT_ID(DOOR_ID));
        GameObject validPost = Objects.get(Predicates.OBJECT_ID(POST_VALID));

        if (validPost != null) {
            if (validPost.isOnScreen() && validPost.applyAction("Tag")) {
                return random(2000, 3000);
            } else {
                Camera.turnTo(validPost);
                return random(100, 200);
            }
        }

        if (Widgets.canContinue()) {
            Widgets.clickContinue();
            return 1000;
        }

        if (exit != null) {
            if (exit.isOnScreen() && exit.applyAction("Exit")) {
                return random(2000, 3000);
            } else {
                Camera.turnTo(exit);
                return random(100, 200);
            }
        }
        return -1;
    }

    private boolean idle() {
        return !Players.getLocal().isMoving() &&
                Players.getLocal().getAnimation() == -1;
    }
}
