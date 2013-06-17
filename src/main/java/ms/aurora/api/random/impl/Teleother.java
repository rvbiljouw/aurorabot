package ms.aurora.api.random.impl;

import ms.aurora.api.methods.Widgets;
import ms.aurora.api.methods.tabs.Options;
import ms.aurora.api.random.AfterLogin;
import ms.aurora.api.random.Random;
import ms.aurora.api.random.RandomManifest;
import ms.aurora.api.wrappers.Widget;

/**
 * @author tobiewarburton
 */
@AfterLogin
@RandomManifest(name = "Teleother", version = 1.0)
public class Teleother extends Random {
    @Override
    public boolean activate() {
        Widget tele = Widgets.getWidget(326, 2);
        return tele != null && tele.getText().contains("wants to teleport");
    }

    @Override
    public int loop() {
        Widget no = Widgets.getWidget(326, 8);
        if (no != null) {
            no.click(true);
        }
        Options.toggleAcceptAid();
        return -1;
    }
}
