package ms.aurora.api.methods.tabs;

import ms.aurora.api.methods.Widgets;
import ms.aurora.api.wrappers.RSWidget;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

/**
 * @author tobiewarburton
 */
public class Tabs {

    public static boolean openTab(Tab tab) {
        if (isOpen(tab)) return true;
        for (RSWidget widget : getTabWidgets()) {
            if (widget.getId() == tab.getId()) {
                return widget.click(true);
            }
        }
        return false;
    }

    public static boolean isOpen(Tab tab) {
        for (RSWidget widget : getTabWidgets()) {
            if (widget.getTextureId() > 0) {
                for (Tab t : Tab.values()) {
                    if (t == tab && widget.getId() == tab.getId()) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private static RSWidget[] getTabWidgets() {
        List<RSWidget> tabs = newArrayList();
        for (Tab tab : Tab.values()) {
            tabs.add(Widgets.getWidget(548, tab.getId()));
        }
        return tabs.toArray(new RSWidget[]{});
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
    }
}
