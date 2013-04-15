package ms.aurora.api.methods.tabs;

import com.google.common.base.Function;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.Maps;
import com.google.common.collect.Multiset;
import ms.aurora.api.methods.Widgets;
import ms.aurora.api.wrappers.RSWidget;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static com.google.common.collect.Lists.newArrayList;
import static com.google.common.collect.Lists.transform;

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
        final Map<RSWidget, Integer> widgetTextureMap = Maps.toMap(getTabWidgets(),
                new Function<RSWidget, Integer>() {
                    @Override
                    public Integer apply(RSWidget input) {
                        return input.getTextureId();
                    }
                }
        );
        final Map<Integer, Integer> textureFrequencyMap = Maps.toMap(widgetTextureMap.values(),
                new Function<Integer, Integer>() {
                    @Override
                    public Integer apply(Integer input) {
                        return Collections.frequency(widgetTextureMap.values(), input);
                    }
                }
        );
        assert textureFrequencyMap.size() == 2 : "textures on inventory is fucked";
        for (Map.Entry<RSWidget, Integer> widgetTexture : widgetTextureMap.entrySet()) {
            int frequency = textureFrequencyMap.get(widgetTexture.getValue());
            if (frequency == 1) {
                int id = widgetTexture.getKey().getId();
                return Tab.byId(id);
            }
        }
        // the following should NEVER happen while logged in, if it does we have a problem
        return null;
    }

    private static List<RSWidget> getTabWidgets() {
        List<RSWidget> tabs = newArrayList();
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
