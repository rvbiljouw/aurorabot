package ms.aurora.api.methods;

import ms.aurora.api.Context;

/**
 * Created with IntelliJ IDEA.
 * User: tobiewarburton
 * Date: 31/03/13
 * Time: 14:11
 * To change this template use File | Settings | File Templates.
 */
public class Settings {

    public static int getSetting(int index) {
        return Context.get().getClient().getWidgetSettings()[index];
    }

}
