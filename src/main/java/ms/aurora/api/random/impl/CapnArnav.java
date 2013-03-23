package ms.aurora.api.random.impl;

import ms.aurora.api.random.Random;

/**
 * @author tobiewarburton
 */
public class CapnArnav extends Random {
    @Override
    public boolean activate() {
        return false;
    }

    @Override
    public int loop() {
        return 0;
    }
}
