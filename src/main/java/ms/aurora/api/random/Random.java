package ms.aurora.api.random;

import ms.aurora.api.Context;

/**
 * @author tobiewarburton
 */
public abstract class Random extends Context {
    public abstract boolean activate();

    public abstract int loop();
}
