package ms.aurora.api.methods.tabs;

import ms.aurora.api.ClientContext;
import ms.aurora.api.wrappers.RSWidget;
import ms.aurora.api.wrappers.RSWidgetGroup;
import ms.aurora.rt3.Widget;

/**
 * @author rvbiljouw
 */
public class Prayer {
    private static final int PRAYER_BASE = 297;
    // Check if prayer is toggled by it's base ids.
    private final ClientContext ctx;

    public Prayer(ClientContext ctx) {
        this.ctx = ctx;
    }

    private RSWidgetGroup getPrayerWidget() {
        ctx.tabs.openTab(Tabs.Tab.PRAYER);
        return ctx.widgets.getWidgets(PRAYER_BASE);
    }

    public boolean isPrayerActive(Pray pray) {
        RSWidget prayerWidget = ctx.widgets.getWidget(PRAYER_BASE, pray.id);
        return prayerWidget != null && prayerWidget.getBackgroundColor() != -1;
    }

    public boolean togglePrayer(Pray pray) {
        RSWidget prayerWidget = ctx.widgets.getWidget(PRAYER_BASE, pray.id);
        if(prayerWidget != null) {
            return prayerWidget.click(true);
        }
        return false;
    }

    public static enum Pray {
        THICK_SKIN(4),
        BURST_OF_STRENGTH(6),
        CLARITY_OF_THOUGHT(8),
        SHARP_EYE(10),
        MYSTIC_WILL(12),
        ROCK_SKIN(14),
        SUPERHUMAN_STRENGTH(16),
        IMPROVED_REFLEXES(18),
        RAPID_RESTORE(20),
        RAPID_HEAL(22),
        PROTECT_ITEM(24),
        HAWK_EYE(26),
        MYSTIC_LORE(28),
        STEEL_SKIN(30),
        ULTIMATE_STRENGTH(32),
        INCREDIBLE_REFLEXES(34),
        PROTECT_FROM_MAGIC(36),
        PROTECT_FROM_MISSILES(38),
        PROTECT_FROM_MELEE(40),
        EAGLE_EYE(42),
        MYSTIC_MIGHT(44),
        RETRIBUTION(46),
        REDEMPTION(48),
        SMITE(50),
        CHIVALRY(52),
        PIETY(54);

        int id;

        Pray(int id) {
            this.id = id;
        }

    }

    public static class PrayerPointException extends RuntimeException {

        public PrayerPointException(Pray pray) {
            super("Not enough prayer points for prayer " + pray.name());
        }

    }
}
