package ms.aurora.api.random.impl;

import ms.aurora.api.methods.Objects;
import ms.aurora.api.methods.Players;
import ms.aurora.api.methods.Widgets;
import ms.aurora.api.random.Random;
import ms.aurora.api.wrappers.RSObject;
import ms.aurora.input.VirtualKeyboard;

import java.awt.event.KeyEvent;

import static ms.aurora.api.methods.filters.ObjectFilters.ID;
import static ms.aurora.api.util.Utilities.random;
import static ms.aurora.api.util.Utilities.sleepNoException;

/**
 * @author rvbiljouw
 */
public class Pinball extends Random {
    private final int POST_G[] = {15000, 15004, 15006, 15008, 15002};
    private final int POST_NG[] = {15007, 15005, 15003, 15009, 15001};

    @Override
    public boolean activate() {
        return Objects.get(ID(POST_NG)) != null;
    }

    @Override
    public int loop() {
        if (!idle()) return random(1000, 2000);

        RSObject exit = Objects.get(ID(15010));
        if (Objects.get(ID(POST_NG)) != null) {
            RSObject post = Objects.get(ID(POST_G));
            if (post != null && post.isOnScreen()) {
                if(post.applyAction("Tag")) {
                    return random(2000, 3000);
                }
                return random(1, 2);
            }
        }

        if (Widgets.canContinue()) {
            Widgets.clickContinue();
            return 1000;
        }

        if (exit != null) {
            VirtualKeyboard.holdKey((char)KeyEvent.VK_LEFT);
            long time = System.currentTimeMillis();
            while(!exit.isOnScreen() && (System.currentTimeMillis() - time) < 5000) {
                sleepNoException(30);
            }
            VirtualKeyboard.releaseKey((char)KeyEvent.VK_LEFT);
            exit.applyAction("Exit");
            return random(600, 1200);
        }
        return -1;
    }

    private boolean idle() {
        return !Players.getLocal().isMoving() &&
                Players.getLocal().getAnimation() == -1;
    }
}
