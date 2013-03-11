package ms.aurora.api.wrappers;

import ms.aurora.api.ClientContext;
import ms.aurora.api.rt3.Renderable;

/**
 * @author rvbiljouw
 */
public class RSRenderable {
    public final ClientContext context;
    private final Renderable wrapped;

    public RSRenderable(ClientContext context, Renderable wrapped) {
        this.context = context;
        this.wrapped = wrapped;
    }

    public int getHeight() {
        return -(wrapped.getHeight() / 2);
    }

}
