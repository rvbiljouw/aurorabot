package ms.aurora.api.methods.tabs;

import ms.aurora.api.Context;
import ms.aurora.api.methods.Widgets;
import ms.aurora.api.wrappers.RSWidgetGroup;
import org.apache.log4j.Logger;

/**
 * @author rvbiljouw
 * @author tobiewarburton
 */
public class Options {
    private final static Logger logger = Logger.getLogger(Options.class);
    private final int runIndex = 173; // stolen rofl

    private static RSWidgetGroup getSettingsGroup() {
        return Widgets.getWidgets(261);
    }

    public static int get(int index) {
        int[] settings = Context.get().getClient().getWidgetSettings();
        return settings != null && index < settings.length ? settings[index] : -1;
    }


    public static void toggleRun() {
        Tabs.openTab(Tabs.Tab.OPTIONS);
        getSettingsGroup().getWidgets()[0].click(true);
    }

    public static void toggleAcceptAid() {
        Tabs.openTab(Tabs.Tab.OPTIONS);
        getSettingsGroup().getWidgets()[4].click(true);
    }
}
