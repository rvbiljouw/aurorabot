package ms.aurora.api.random.impl;

import ms.aurora.api.methods.tabs.Inventory;
import ms.aurora.api.methods.tabs.Options;
import ms.aurora.api.random.Random;

import static ms.aurora.api.util.Utilities.random;
import static ms.aurora.api.util.Utilities.sleepNoException;

/**
 * @author tobiewarburton
 */
public class StrangeBox extends Random {
    @Override
    public boolean activate() {
        return Inventory.contains(3062);
    }

    int boxBeforeSolution;

    @Override
    public int loop() {
        if (Options.get(312) != 0 && Options.get(312) != boxBeforeSolution) {
            int boxSolution = (boxBeforeSolution = (Options.get(312) >> 24) & 0xff);
            if (boxSolution == 0) {
                get().input.getMouse().clickMouse(random(118, 185), random(284, 315), true);
            } else if (boxSolution == 1) {
                get().input.getMouse().clickMouse(random(237, 304), random(284, 315), true);
            } else if (boxSolution == 2) {
                get().input.getMouse().clickMouse(random(352, 419), random(284, 315), true);
            }
            sleepNoException(random(400, 600));
            boxBeforeSolution = -1;
            return -1;
        } else {
            Inventory.get(3062).applyAction("Open");
        }
        return random(400, 600);
    }
}
