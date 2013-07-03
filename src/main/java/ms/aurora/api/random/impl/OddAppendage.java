package ms.aurora.api.random.impl;

import ms.aurora.api.methods.Objects;
import ms.aurora.api.methods.Players;
import ms.aurora.api.methods.Widgets;
import ms.aurora.api.random.AfterLogin;
import ms.aurora.api.random.Random;
import ms.aurora.api.random.RandomManifest;
import ms.aurora.api.util.StatePredicate;
import ms.aurora.api.util.Utilities;
import ms.aurora.api.wrappers.GameObject;

/**
 * Date: 02/07/13
 * Time: 10:22
 *
 * @author A_C/Cov
 */
@AfterLogin
@RandomManifest(name = "Odd Appandage", version = 1.0, author = "Cov")
public class OddAppendage extends Random {

    private static final int[] LEVER_IDS = { 12724, 12723, 12722, 12725 };

    private GameObject lever = null;

    @Override
    public boolean activate() {
        lever = null;
        return Objects.find().id(LEVER_IDS).onScreen().result().length > 0;
    }

    @Override
    public int loop() {
        while (Widgets.canContinue()) {
            Widgets.clickContinue();
            Utilities.sleepNoException(500);
        }

        lever = Objects.find().id(LEVER_IDS[0]).onScreen().single();
        if (lever != null) {
            if (lever.applyAction("Operate")) {
                Utilities.sleepWhile(new StatePredicate() {
                    @Override
                    public boolean apply() {
                        return Players.getLocal().isIdle();
                    }
                }, 3000);
            }
        }
        return -1;
    }

}
