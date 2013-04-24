package ms.aurora.api.methods.tabs;

import ms.aurora.api.methods.Widgets;
import ms.aurora.api.wrappers.RSWidget;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author tobiewarburton
 */
public class Tabs {
    /**
     * opens a tab
     *
     * @param tab the tab to open
     * @return true on success else false
     */
    public static boolean openTab(Tab tab) {
        if (isOpen(tab)) return true;
        for (RSWidget widget : getTabWidgets()) {
            if (widget.getId() == tab.getId()) {
                return widget.click(true);
            }
        }
        return false;
    }

    /**
     * checks if the specified tab is open
     *
     * @param tab the tab to check if it's selected
     * @return true if tab is selected else false
     */
    public static boolean isOpen(Tab tab) {
        Tab current = getCurrent();
        return current == tab;
    }

    /**
     * gets the currently selected tab
     *
     * @return the current selected tab
     */
    public static Tab getCurrent() {
        return obtainOpen(getTabWidgets());
    }

    private static Tab obtainOpen(List<RSWidget> widgetList) {
        RSWidget[] widgets = widgetList.toArray(new RSWidget[widgetList.size()]);
        int targetTextureID = getOpenTextureID(widgets);
        for (RSWidget widget : widgets) {
            if (widget.getTextureId() == targetTextureID) {
                return Tab.byId(widget.getId());
            }
        }
        return null;
    }

    private static int getOpenTextureID(RSWidget[] widgets) {
        int[] textureIDs = new int[widgets.length];
        Map<Integer, Integer> frequency = new HashMap<Integer, Integer>();
        for (int i = 0; i < widgets.length; i++) {
            textureIDs[i] = widgets[i].getTextureId();
            if (frequency.containsKey(textureIDs[i])) {
                int freq = frequency.get(textureIDs[i]);
                frequency.remove(textureIDs[i]);
                frequency.put(textureIDs[i], freq + 1);
            } else {
                frequency.put(textureIDs[i], 1);
            }
        }
        return extractSingleton(frequency);
    }

    private static <T> T extractSingleton(Map<T, Integer> items) {
        for (Map.Entry<T, Integer> entries : items.entrySet()) {
            if (entries.getValue() == 1) {
                return entries.getKey();
            }
        }
        return null;
    }

    private static List<RSWidget> getTabWidgets() {
        List<RSWidget> tabs = new ArrayList<RSWidget>();
        for (Tab tab : Tab.values()) {
            tabs.add(Widgets.getWidget(548, tab.getId()));
        }
        return tabs;
    }

    public static enum Tab {
        /**
         * 47 - 53 - Top Bar
         * COMBAT, STATS, QUESTS, INVENTORY, EQUIPMEN, PRAYER, MAGIC
         * 30 - 36 - Bottom Bar
         * CLAN_CHAT, FRIENDS_LISTm IGNORE_LIST, LOGOUT, OPTIONS, EMOTES, MUSIC
         * texture | 861580522 ( OPEN )
         * texture | -1347704871 ( CLOSED )
         */

        COMBAT(47),
        STATS(48),
        QUESTS(49),
        INVENTORY(50),
        EQUIPMENT(51),
        PRAYER(52),
        MAGIC(53),
        CLAN_CHAT(30),
        FRIENDS_LIST(31),
        IGNORE_LIST(32),
        LOGOUT(33),
        OPTIONS(34),
        EMOTES(35),
        MUSIC(36);

        int id;

        private Tab(int id) {
            this.id = id;
        }

        public int getId() {
            return id;
        }

        public static Tab byId(int id) {
            for (Tab t : values()) {
                if (t.getId() == id) {
                    return t;
                }
            }
            return null;
        }
    }
}
