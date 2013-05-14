package ms.aurora.api.random.impl;

import ms.aurora.api.methods.Camera;
import ms.aurora.api.methods.Objects;
import ms.aurora.api.methods.Players;
import ms.aurora.api.methods.Widgets;
import ms.aurora.api.random.AfterLogin;
import ms.aurora.api.random.Random;
import ms.aurora.api.wrappers.RSObject;
import ms.aurora.input.VirtualKeyboard;

import java.awt.event.KeyEvent;

import static ms.aurora.api.methods.filters.ObjectFilters.ID;
import static ms.aurora.api.util.Utilities.random;
import static ms.aurora.api.util.Utilities.sleepNoException;

/**
 * Solves the pinball random event
 *
 * @author rvbiljouw
 */
@AfterLogin
public class Pinball extends Random {
    private final int POST_VALID[] = {15000, 15002, 15004, 15006, 15008};
    private final int POST_INVALID[] = {15001, 15003, 15005, 15007, 15009};
    private static final int DOOR_ID = 15010;

    @Override
    public boolean activate() {
        return Objects.get(ID(POST_INVALID)) != null;
    }

    @Override
    public int loop() {
        if (!idle()) return random(1000, 2000);
        RSObject exit = Objects.get(ID(DOOR_ID));
        RSObject validPost = Objects.get(ID(POST_VALID));

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
