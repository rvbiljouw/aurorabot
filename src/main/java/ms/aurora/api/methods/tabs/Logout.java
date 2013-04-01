package ms.aurora.api.methods.tabs;

import ms.aurora.api.methods.Widgets;
import ms.aurora.api.wrappers.RSWidget;
import ms.aurora.api.wrappers.RSWidgetGroup;
import org.apache.log4j.Logger;

/**
 * @author rvbiljouw
 * @author tobiewarburton
 */
public class Logout {
    private final static Logger logger = Logger.getLogger(Logout.class);

    private static RSWidgetGroup getLogoutGroup() {
        return Widgets.getWidgets(182);
    }

    public static void logout() {
        Tabs.openTab(Tabs.Tab.LOGOUT);
        for (RSWidget child : getLogoutGroup().getWidgets()) {
            if (child.getText().equals("Click here to logout")) {
                child.click(true);
                return;
            }
        }
    }
}
