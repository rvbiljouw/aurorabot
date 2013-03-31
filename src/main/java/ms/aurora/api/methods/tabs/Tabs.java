package ms.aurora.api.methods.tabs;

import ms.aurora.api.ClientContext;
import ms.aurora.api.wrappers.RSWidget;

/**
 * @author tobiewarburton
 */
public class Tabs {
    private final ClientContext ctx;

    public Tab current;

    public Tabs(ClientContext ctx) {
        this.ctx = ctx;
    }


    public boolean openTab(Tab tab) {
        if (getCurrent() != null && getCurrent().equals(tab)) return true;
        RSWidget[] widgets = ctx.widgets.getWidgets(548).getWidgets();
        for (int i = 0; i < widgets.length; i++) {
            RSWidget widget = widgets[i];
            if (widget.getActions() != null) {
                String[] actions = widget.getActions();
                for (int j = 0; j < actions.length; j++) {
                    String action = actions[j];
                    if (action.equals(tab.getName())) {
                        widget.click(true);
                        this.current = tab;
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public Tab getCurrent() {
        return this.current;
    }

    public static enum Tab {

        COMBAT("COMBAT", 0, "Combat Options"),
        STATS("STATS", 1, "Stats"),
        QUESTS("QUESTS", 2, "Quest List"),
        INVENTORY("INVENTORY", 3, "Inventory"),
        EQUIPMENT("EQUIPMENT", 4, "Worn Equipment"),
        PRAYER("PRAYER", 5, "Prayer"),
        MAGIC("MAGIC", 6, "Magic"),
        CLAN_CHAT("CLAN_CHAT", 7, "Clan Chat"),
        FRIENDS_LIST("FRIENDS_LIST", 8, "Friends List"),
        IGNORE_LIST("IGNORE_LIST", 9, "Ignore List"),
        LOGOUT("LOGOUT", 10, "Logout"),
        OPTIONS("OPTIONS", 11, "Options"),
        EMOTES("EMOTES", 12, "Emotes"),
        MUSIC("MUSIC", 13, "Music Player");

        String name;

        private static final Tab[] ALL = new Tab[]{COMBAT, STATS, QUESTS, INVENTORY, EQUIPMENT, PRAYER, MAGIC, CLAN_CHAT, FRIENDS_LIST, IGNORE_LIST, LOGOUT, OPTIONS, EMOTES, MUSIC};


        private Tab(String var1, int var2, String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }
    }
}
