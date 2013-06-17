package ms.aurora.api.methods.tabs;

import ms.aurora.api.methods.Widgets;
import ms.aurora.api.wrappers.Widget;

/**
 * Logout tab functions
 *
 * @author Rick
 * @author tobiewarburton
 */
public final class Logout {

    /**
     * Logs out the player
     */
    public static void logout() {
        if (!Tabs.isOpen(Tabs.Tab.LOGOUT)) {
            Tabs.openTab(Tabs.Tab.LOGOUT);
        }
        Widget logout = Widgets.getWidget(Constants.LOGOUT_TAB, Constants.LOGOUT_BTN);
        if (logout != null) {
            logout.click(true);
        }
    }

    public static class Constants {
        public static final int LOGOUT_TAB = 182;
        public static final int LOGOUT_BTN = 6;
    }
}
