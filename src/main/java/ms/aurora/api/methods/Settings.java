package ms.aurora.api.methods;

import ms.aurora.api.ClientContext;

/**
 * Created with IntelliJ IDEA.
 * User: tobiewarburton
 * Date: 31/03/13
 * Time: 14:11
 * To change this template use File | Settings | File Templates.
 */
public class Settings {
    private ClientContext ctx;

    public Settings(ClientContext ctx) {
        this.ctx = ctx;
    }

    public int getSetting(int index) {
        return ctx.getClient().getWidgetSettings()[index];
    }
}
