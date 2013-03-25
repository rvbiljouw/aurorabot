package ms.aurora.api.random.impl;

import ms.aurora.api.methods.Widgets;
import ms.aurora.api.random.Random;
import ms.aurora.api.wrappers.RSWidget;

/**
 * @author tobiewarburton
 */
public class WelcomeScreen extends Random {
    @Override
    public boolean activate() {
        return widgets.getWidget(378, 45) != null;
    }

    @Override
    public int loop() {
        RSWidget play = widgets.getWidget(378, 45);
        if (play != null) {
            play.click(true);
        }
        return -1;
    }
}
