package ms.aurora.api.random.impl;

import ms.aurora.api.methods.Settings;
import ms.aurora.api.methods.Widgets;
import ms.aurora.api.methods.filters.Filters;
import ms.aurora.api.methods.tabs.Inventory;
import ms.aurora.api.methods.tabs.Tabs;
import ms.aurora.api.random.AfterLogin;
import ms.aurora.api.random.Random;
import ms.aurora.api.random.RandomManifest;
import ms.aurora.api.util.Predicate;
import ms.aurora.api.util.Utilities;
import ms.aurora.api.wrappers.Widget;
import ms.aurora.api.wrappers.WidgetItem;

/**
 * @author iJava
 */
@AfterLogin
@RandomManifest(name = "Lamp", version = 1.0)
public class Lamp extends Random {

    private static final int LAMP_ID = 2529;
    private static final int CONFIRM_ID = 26;
    private static final int PARENT_ID = 134;
    private static final int SETTING = 261;

    @Override
    public boolean activate() {
        return Tabs.isOpen(Tabs.Tab.INVENTORY) && Inventory.contains(Filters.WIDGETITEM_ID(LAMP_ID));
    }

    @Override
    public int loop() {
        if (Widgets.getWidget(PARENT_ID, CONFIRM_ID) == null) {
                   WidgetItem lamp = Inventory.get(Filters.WIDGETITEM_ID(LAMP_ID));
                   if (lamp != null) {
                       if (lamp.applyAction("Rub")) {
                           return 800;
                       }
                   }
                   return 300;
               }
              // int widgetIndex = Choice.getWidgetIndex(getSession().getAccount().);
               int widgetIndex = 5;
               Widget choice = Widgets.getWidget(PARENT_ID, widgetIndex);
               if (choice != null) {
                   if (Settings.get(SETTING) == (widgetIndex - 2)) {
                       Widget confirm = Widgets.getWidget(PARENT_ID, CONFIRM_ID);
                       if (confirm != null) {
                           confirm.click(true);
                           return 1000;
                       }
                       return 500;
                   } else {
                   choice.click(true);
                   return 1000;
                   }
               }
               return 500;
    }

    private static enum Choice {
        ATTACK("Attack", 3), STRENGTH("Strength", 4), RANGED("Ranged", 5), MAGIC("Magic", 6),
        DEFENCE("Defence", 7), HITPOINTS("Constitution", 8), PRAYER("Prayer", 9), AGILITY("Agility", 10),
        HERBLORE("Herblore", 11), THIEVING("Thieving", 12), CRAFTING("Crafting", 13), RUNECRAFT("Runecrafting", 14),
        MINING("Mining", 15), SMITHING("Smithing", 16), FISHING("Fishing", 17), COOKING("Cooking", 18), FIREMAKING("Firemaking", 19),
        WOODCUTTING("Woodcutting", 20), FLETCHING("Fletching", 21), SLAYER("Slayer", 22), FARMING("Farming", 23),
        CONSTRUCTION("Construction", 24), HUNTER("Hunter", 25);
        private String name;
        private int widgetIndex;

        private Choice(String name, int widgetIndex) {
            this.name = name;
            this.widgetIndex = widgetIndex;
        }

        public static int getWidgetIndex(String skill) {
            for (Choice choice : Choice.values()) {
                if (choice.getName().equals(skill)) {
                    return choice.getWidgetIndex();
                }
            }
            return -1;
        }

        public String getName() {
            return name;
        }

        public int getWidgetIndex() {
            return widgetIndex;
        }
    }
}
