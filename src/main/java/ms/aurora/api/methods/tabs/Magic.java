package ms.aurora.api.methods.tabs;

import ms.aurora.api.methods.Widgets;
import ms.aurora.api.wrappers.Widget;
import ms.aurora.api.wrappers.WidgetGroup;
import org.apache.log4j.Logger;

/**
 * @author Rick
 * @author tobiewarburton
 */
public final class Magic {
    private final static Logger logger = Logger.getLogger(Magic.class);

    private static WidgetGroup getSpellGroup() {
        return Widgets.getWidgetGroup(192);
    }

    /**
     * Casts the spell specified.
     * @param spell spell to be cast.
     */
    public static void castSpell(Spell spell) {
        Tabs.openTab(Tabs.Tab.MAGIC);
        Widget spellButton = getSpellGroup().getWidgets()[spell.id];
        if (spellButton != null) {
            spellButton.click(true);
        } else {
            logger.error("Failed to cast spell " + spell.name() + ": no such interface.");
        }
    }

    public static enum Spell {

        HOME_TELEPORT(0),
        WIND_STRIKE(1),
        CONFUSE(2),
        ENCHANT_CBOW_BOLT(3),
        WATER_STRIKE(4),
        LVL_1_ENCHANT(5),
        EARTH_STRIKE(6),
        WEAKEN(7),
        FIRE_STRIKE(8),
        BONES_TO_BANANAS(9),
        WIND_BOLT(10),
        CURSE(11),
        BLIND(12),
        LOW_LEVEL_ALCHEMY(13),
        WATER_BOLT(14),
        VARROCK_TELEPORT(15),
        LVL_2_ENCHANT(16),
        EARTH_BOLT(17),
        LUMBRIDGE_TELEPORT(18),
        TELEKINETIC_GRAB(19),
        FIRE_BOLT(20),
        FALADOR_TELEPORT(21),
        CRUMBLE_UNDEAD(22),
        TELEPORT_TO_HOUSE(23),
        WIND_BLAST(24),
        SUPERHEAT_ITEM(25),
        CAMELOT_TELEPORT(26),
        WATER_BLAST(27),
        LVL_3_ENCHANT(28),
        IBAN_BLAST(29),
        SNARE(30),
        MAGIC_DART(31),
        ARDOUNGNE_TELEPORT(32),
        EARTH_BLAST(33),
        HIGH_LEVEL_ALCHEMY(34),
        CHARGE_WATER_ORB(35),
        LVL_4_ENCHANT(36),
        WATCHTOWER_TELEPORT(37),
        FIRE_BLAST(38),
        CHARGE_EARTH_ORB(39),
        BONES_TO_PEACHES(40),
        SARADOMIN_STRIKE(41),
        CLAWS_OF_GUTHIX(42),
        FLAMES_OF_ZAMORAK(43),
        TROLLHEIM_TELEPORT(44),
        WIND_WAVE(45),
        CHARGE_FIRE_ORB(46),
        TELEPORT_TO_APE_ATOLL(47),
        WATER_WAVE(48),
        CHARGE_AIR_ORB(49),
        VULNERABILITY(50),
        LVL_5_ENCHANT(51),
        EARTH_WAVE(52),
        ENFEEBLE(53),
        TELEOTHER_LUMBRIDGE(54),
        FIRE_WAVE(55),
        ENTANGLE(56),
        STUN(57),
        CHARGE(58),
        TELEOTHER_FALADOR(59),
        TELE_BLOCK(60),
        LVL_6_ENCHANT(61),
        TELEOTHER_CAMELOT(62);

        int id;

        Spell(int id) {
            this.id = id;
        }

    }
}
