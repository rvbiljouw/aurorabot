package ms.aurora.api.methods.tabs;

import ms.aurora.api.Context;
import ms.aurora.api.methods.Widgets;
import ms.aurora.api.wrappers.RSWidget;
import ms.aurora.api.wrappers.RSWidgetGroup;
import org.apache.log4j.Logger;

/**
 * Combat setting functions
 * @author rvbiljouw
 * @author tobiewarburton
 */
public final class Combat {
    private final static Logger logger = Logger.getLogger(Combat.class);

    private static RSWidgetGroup getCombatGroup() {
        return Widgets.getWidgets(92);
    }

    /**
     * Gets the name of the currently wielded weapon.
     * @return weapon name
     */
    public static String getWeaponName() {
        RSWidget widget = getCombatGroup().getWidgets()[0];
        if (widget != null) {
            return widget.getText();
        }
        return "undefined";
    }

    /**
     * Toggles the auto retaliate button.
     */
    public static void toggleAutoRetaliate() {
        Tabs.openTab(Tabs.Tab.COMBAT);
        for (RSWidget child : getCombatGroup().getWidgets()) {
            if (child.getText().contains("Auto Retaliate")) {
                child.applyAction(" ");
                return;
            }
        }
    }

    /**
     * Toggles the special attack bar
     */
    public static void toggleSpecialAttack() {
        Tabs.openTab(Tabs.Tab.COMBAT);
        for (RSWidget child : getCombatGroup().getWidgets()) {
            if (child.getText().contains("S P E C I A L  A T T A C K")) {
                child.applyAction(" ");
                return;
            }
        }
    }

    /**
     * Selects a combat style.
     * @param style the style to select for example Aggressive
     */
    public static void selectCombatStyle(String style) {
        Tabs.openTab(Tabs.Tab.COMBAT);
        for (RSWidget child : getCombatGroup().getWidgets()) {
            if (child.getText().contains(style)) {
                child.applyAction(" ");
                return;
            }
        }
    }

}
