package ms.aurora.api.random.impl;

import ms.aurora.api.random.Random;

/**
 * @author tobiewarburton
 */
//TODO interface ids and such and account manager
public class AutoLogin extends Random {
    @Override
    public boolean activate() {
        return getClient().getLoginIndex() == 10;
    }

    @Override
    public int loop() {
        return -1;
    }
}
