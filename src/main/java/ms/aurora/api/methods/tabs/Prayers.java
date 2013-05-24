package ms.aurora.api.methods.tabs;

import ms.aurora.api.methods.Widgets;
import ms.aurora.api.wrappers.RSWidget;

/**
 * @author Matty Cov / A_C
 * @author Rick
 */
public final class Prayers {

    public interface Prayer {

        /**
         * @return returns child interface of the prayer
         */
        public int getInterface();

        /**
         * @return Level required to activate prayer
         */
        public int getRequiredLvl();

        /**
         * @return Name of prayer as String
         */
        public String getName();

    }

    public enum Modern implements Prayer {

        THICK_SKIN(0, 1, "Thick skin"),
        BURST_OF_STRENGTH(1, 4, "Burst of strength"),
        CLARITY_OF_THOUGHT(2, 7, "Clarity of thought"),
        SHARP_EYE(3, 8, "Sharp eye"),
        MYSTIC_WILL(4, 9, "Mystic will"),
        ROCK_SKIN(5, 10, "Rock skin"),
        SUPERHUMAN_STRENGTH(6, 13, "Superhuman strength"),
        IMPROVED_REFLEXES(7, 16, "Improved reflexes"),
        RAPID_RESTORE(8, 19, "Rapid restore"),
        RAPID_HEAL(9, 22, "Rapid heal"),
        PROTECT_ITEM_REGULAR(10, 25, "Protect item"),
        HAWK_EYE(11, 26, "Hawk eye"),
        MYSTIC_LORE(12, 27, "Mystic lore"),
        STEEL_SKIN(13, 28, "Steel skin"),
        ULTIMATE_STRENGTH(14, 31, "Ultimate strength"),
        INCREDIBLE_REFLEXES(15, 34, "Incredible reflexes"),
        PROTECT_FROM_SUMMONING(16, 35, "Protect from summoning"),
        PROTECT_FROM_MAGIC(17, 37, "Protect from magic"),
        PROTECT_FROM_MISSILES(18, 40, "Protect from missiles"),
        PROTECT_FROM_MELEE(19, 43, "Protect from melee"),
        EAGLE_EYE(20, 44, "Eagle eye"),
        MYSTIC_MIGHT(21, 45, "Mystic might"),
        RETRIBUTION(22, 46, "Retribution"),
        REDEMPTION(23, 49, "Redemption"),
        SMITE(24, 52, "Smite"),
        CHIVALRY(25, 60, "Chivalry"),
        RAPID_RENEWAL(26, 65, "Rapid renewal"),
        PIETY(27, 70, "Piety"),
        RIGOUR(28, 74, "Rigour"),
        AUGURY(29, 77, "Augury");

        private int index;
        private int reqLvl;
        private String name;

        /**
         * Gets the modern book.
         *
         * @param index  Prayer index.
         * @param reqLvl Required level.
         * @param name   Prayer name.
         */
        private Modern(int index, int reqLvl, String name) {
            this.index = index;
            this.reqLvl = reqLvl;
            this.name = name;
        }

        /**
         * Gets the Interface.
         *
         * @return index.
         */
        public int getInterface() {
            return this.index;
        }

        /**
         * Gets the required level.
         *
         * @return Required level.
         */
        public int getRequiredLvl() {
            return this.reqLvl;
        }

        /**
         * Gets the name.
         *
         * @return Name.
         */
        public String getName() {
            return this.name;
        }
    }

    /**
     * Gets a prayer book.
     *
     * @return widget
     */
    public static RSWidget getBook() {
        return Widgets.getWidget(Constants.PRAYER_TAB, Constants.PRAYER_INTERFACE);
    }

    /**
     * Gets a prayer in the prayer book
     *
     * @param p prayer
     * @return toggle widget
     */
    public static RSWidget getPrayer(Prayer p) {
        return getBook().getChildren()[p.getInterface()];
    }

    /**
     * @param p Activates prayer.
     */
    public static void activate(Prayer p) {
        if (!Tabs.isOpen(Tabs.Tab.PRAYER)) {
            Tabs.openTab(Tabs.Tab.PRAYER);
        }
        getPrayer(p).applyAction("Activate");
    }

    /**
     * @param p Deactivates prayer.
     */
    public static void deactivate(Prayer p) {
        if (!Tabs.isOpen(Tabs.Tab.PRAYER)) {
            Tabs.openTab(Tabs.Tab.PRAYER);
        }
        getPrayer(p).applyAction("Deactivate");
    }

    /**
     * @param p Prayer to check
     * @return True - prayer is active else false
     */
    public static boolean isActive(Prayer p) {
        return getPrayer(p).getActions()[0].equals("Deactivate");
    }

    public class Constants {
        public static final int PRAYER_TAB = 271;
        public static final int PRAYER_INTERFACE = 8;
    }
}
