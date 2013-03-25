package ms.aurora.api.methods.tabs;

import ms.aurora.api.ClientContext;
import ms.aurora.api.wrappers.RSWidget;
import ms.aurora.api.wrappers.RSWidgetGroup;
import org.apache.log4j.Logger;

/**
 * @author rvbiljouw
 */
public class Magic {
    private final static Logger logger = Logger.getLogger(Magic.class);
    private final ClientContext ctx;

    public Magic(ClientContext ctx) {
        this.ctx = ctx;
    }

    private RSWidgetGroup getSpellGroup() {
        return ctx.widgets.getWidgets(192);
    }

    public void castSpell(Spell spell) {
        RSWidget spellButton = getSpellGroup().getWidgets()[spell.id];
        if (spellButton != null) {
            spellButton.applyAction("Cast");
        } else {
            logger.error("Failed to cast spell " + spell.name() + ": no such interface.");
        }
    }

    public static enum Spell {

        HOME_TELEPORT(0),
        WIND_STRIKE(1),
        CONFUSE(2),
        ENCHANT_CBOW_4(3);


        int id;

        Spell(int id) {
            this.id = id;
        }

    }
}
