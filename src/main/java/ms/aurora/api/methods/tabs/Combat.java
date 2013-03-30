package ms.aurora.api.methods.tabs;

import ms.aurora.api.ClientContext;
import ms.aurora.api.wrappers.RSWidget;
import ms.aurora.api.wrappers.RSWidgetGroup;
import org.apache.log4j.Logger;

/**
 * @author rvbiljouw
 * @author tobiewarburton
 */
public class Combat {
    private final static Logger logger = Logger.getLogger(Combat.class);
    private final ClientContext ctx;

    public Combat(ClientContext ctx) {
        this.ctx = ctx;
    }

    private RSWidgetGroup getCombatGroup() {
        return ctx.widgets.getWidgets(92);
    }

    public String getWeaponName() {
        RSWidget widget = getCombatGroup().getWidgets()[0];
        if (widget != null) {
            return widget.getText();
        }
        return "undefined";
    }

    public void toggleAutoRetaliate() {
        ctx.tabs.openTab(Tabs.Tab.COMBAT);
        for (RSWidget child : getCombatGroup().getWidgets()) {
            if (child.getText().contains("Auto Retaliate")) {
                child.applyAction(" ");
                return;
            }
        }
    }

    public void toggleSpecialAttack() {
        ctx.tabs.openTab(Tabs.Tab.COMBAT);
        for (RSWidget child : getCombatGroup().getWidgets()) {
            if (child.getText().contains("S P E C I A L  A T T A C K")) {
                child.applyAction(" ");
                return;
            }
        }
    }

    /**
     *
     * @param style the style to select for example Aggressive
     */
    public void selectCombatStyle(String style) {
        ctx.tabs.openTab(Tabs.Tab.COMBAT);
        for (RSWidget child : getCombatGroup().getWidgets()) {
            if (child.getText().contains(style)) {
                child.applyAction(" ");
                return;
            }
        }
    }

}
