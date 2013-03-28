package ms.aurora.api.methods.tabs;

import ms.aurora.api.ClientContext;
import ms.aurora.api.wrappers.RSWidget;
import ms.aurora.api.wrappers.RSWidgetGroup;
import org.apache.log4j.Logger;

/**
 * @author rvbiljouw
 */
public class Combat {
    private final static Logger logger = Logger.getLogger(Combat.class);
    private final ClientContext ctx;

    public Combat(ClientContext ctx) {
        this.ctx = ctx;
    }

    private RSWidgetGroup getCombatGroup() {
        return ctx.widgets.getWidgets(89);
    }

    public String getWeaponName() {
        RSWidget widget = getCombatGroup().getWidgets()[0];
        if(widget != null) {
            return widget.getText();
        }
        return "undefined";
    }

    public void toggleAutoRetaliate() {
        for(RSWidget child : getCombatGroup().getWidgets()) {
            if(child.getText().contains("Auto Retaliate")) {
                child.applyAction(" ");
                return;
            }
        }
    }

    // TODO: Attack styles.

}
