package ms.aurora.api.random.impl;

import ms.aurora.api.methods.Settings;
import ms.aurora.api.methods.Widgets;
import ms.aurora.api.methods.Predicates;
import ms.aurora.api.methods.tabs.Inventory;
import ms.aurora.api.random.AfterLogin;
import ms.aurora.api.random.Random;
import ms.aurora.api.random.RandomManifest;
import ms.aurora.api.wrappers.Widget;

import static ms.aurora.api.util.Utilities.random;
import static ms.aurora.api.util.Utilities.sleepNoException;

/**
 * Solves the strange box random.
 *
 * @author tobiewarburton
 */
@AfterLogin
@RandomManifest(name = "Strange Box", version = 1.0)
public class StrangeBox extends Random {

    @Override
    public boolean activate() {
        return Inventory.contains(Predicates.WIDGETITEM_ID(3062));
    }

    @Override
    public int loop() {
        Widget question = Widgets.getWidget(190, 6);
        if (question != null) {
            int boxSolution = (Settings.get(312) >> 24) & 0xff;
            Widget option = Widgets.getWidget(190, 10 + boxSolution);
            option.click(true);
            sleepNoException(random(400, 600));
            return -1;
        } else {
            Inventory.get(Predicates.WIDGETITEM_ID(3062)).applyAction("Open");
        }
        return random(400, 600);
    }

}
