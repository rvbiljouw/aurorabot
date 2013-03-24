package ms.aurora.api.random;

import ms.aurora.api.ClientContext;

/**
 * @author tobiewarburton
 */
public abstract class Random extends ClientContext {
    public abstract boolean activate();

    public abstract int loop();
}
