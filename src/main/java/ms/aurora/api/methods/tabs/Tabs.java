package ms.aurora.api.methods.tabs;

import ms.aurora.api.methods.Widgets;
import ms.aurora.api.wrappers.RSWidget;

/**
 * @author tobiewarburton
 */
public class Tabs {
    private static ThreadLocal<Tab> current = new ThreadLocal<Tab>();

    public static boolean openTab(Tab tab) {
        if (getCurrent() != null && getCurrent().equals(tab)) return true;
        RSWidget[] widgets = Widgets.getWidgets(548).getWidgets();
        for (RSWidget widget : widgets) {
            if (widget.getActions() != null) {
                String[] actions = widget.getActions();
                for (String action : actions) {
                    if (action.equals(tab.getName())) {
                        widget.click(true);
                        current.set(tab);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static boolean isOpen(Tab tab) {
        return getCurrent() == tab;
    }

    public static Tab getCurrent() {
        return current.get();
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

        private Tab(String var1, int var2, String name) {
            this.name = name;
        }

        public String getName() {
            return this.name;
        }
    }
}
