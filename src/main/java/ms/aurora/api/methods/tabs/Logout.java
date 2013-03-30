package ms.aurora.api.methods.tabs;

import ms.aurora.api.ClientContext;
import ms.aurora.api.wrappers.RSWidget;
import ms.aurora.api.wrappers.RSWidgetGroup;
import org.apache.log4j.Logger;

/**
 * @author rvbiljouw
 * @author tobiewarburton
 */
public class Logout {
    private final static Logger logger = Logger.getLogger(Logout.class);
    private final ClientContext ctx;

    public Logout(ClientContext ctx) {
        this.ctx = ctx;
    }

    private RSWidgetGroup getLogoutGroup() {
        return ctx.widgets.getWidgets(182);
    }

    public void logout() {
        ctx.tabs.openTab(Tabs.Tab.LOGOUT);
        for (RSWidget child : getLogoutGroup().getWidgets()) {
            if (child.getText().equals("Click here to logout")) {
                child.click(true);
                return;
            }
        }
    }
}
