package ms.aurora.api.random.impl;

import ms.aurora.api.Context;
import ms.aurora.api.methods.Camera;
import ms.aurora.api.methods.Widgets;
import ms.aurora.api.random.Random;
import ms.aurora.api.random.RandomManfiest;
import ms.aurora.api.wrappers.RSWidget;

import static ms.aurora.api.util.Utilities.sleepNoException;

/**
 * @author tobiewarburton
 */
@RandomManfiest(name = "Welcome Screen", version = 1.0)
public class WelcomeScreen extends Random {
    @Override
    public boolean activate() {
        return Widgets.getWidget(378, 6) != null;
    }

    @Override
    public int loop() {
        RSWidget play = Widgets.getWidget(378, 6);
        if (play != null) {
            play.click(true);
            sleepNoException(1000, 3000);
            if (Context.isLoggedIn()) {
                Camera.setPitch(true);
            }
        }
        return 5000;
    }
}
