package ms.aurora.api.random.impl;

import ms.aurora.api.Widgets;
import ms.aurora.api.random.Random;
import ms.aurora.api.wrappers.RSWidget;

/**
 * @author tobiewarburton
 */
public class Teleother extends Random {
    @Override
    public boolean activate() {
        RSWidget tele = widgets.getWidget(326, 2);
        return tele != null && tele.getText().contains("wants to teleport");
    }

    @Override
    public int loop() {
        RSWidget no = widgets.getWidget(326, 8);
        if (no != null) {
            no.click(true);
        }
        //TODO: disable accept aid
        return -1;
    }
}
