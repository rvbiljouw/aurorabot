package ms.aurora.api.methods.tabs;

import ms.aurora.api.ClientContext;
import ms.aurora.api.wrappers.RSWidgetGroup;
import org.apache.log4j.Logger;

/**
 * @author rvbiljouw
 * @author tobiewarburton
 */
public class Options {
    private final static Logger logger = Logger.getLogger(Options.class);
    private final ClientContext ctx;

    private final int runIndex = 173; // stolen rofl

    public Options(ClientContext ctx) {
        this.ctx = ctx;
    }

    private RSWidgetGroup getSettingsGroup() {
        return ctx.widgets.getWidgets(261);
    }

    public int get(int index) {
        int[] settings = this.ctx.getClient().getWidgetSettings();
        return settings != null && index < settings.length ? settings[index] : -1;
    }


    public void toggleRun() {
        ctx.tabs.openTab(Tabs.Tab.OPTIONS);
        getSettingsGroup().getWidgets()[0].click(true);
    }

    public void toggleAcceptAid() {
        ctx.tabs.openTab(Tabs.Tab.OPTIONS);
        getSettingsGroup().getWidgets()[4].click(true);
    }
}
