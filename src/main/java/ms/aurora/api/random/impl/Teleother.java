package ms.aurora.api.random.impl;

import ms.aurora.api.Context;
import ms.aurora.api.methods.Widgets;
import ms.aurora.api.methods.tabs.Options;
import ms.aurora.api.random.AfterLogin;
import ms.aurora.api.random.Random;
import ms.aurora.api.wrappers.RSWidget;

/**
 * @author tobiewarburton
 */
@AfterLogin
public class Teleother extends Random {
    @Override
    public boolean activate() {
        RSWidget tele = Widgets.getWidget(326, 2);
        return tele != null && tele.getText().contains("wants to teleport");
    }

    @Override
    public int loop() {
        RSWidget no = Widgets.getWidget(326, 8);
        if (no != null) {
            no.click(true);
        }
        Options.toggleAcceptAid();
        return -1;
    }
}
