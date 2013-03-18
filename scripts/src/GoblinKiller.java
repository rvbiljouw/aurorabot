import ms.aurora.api.Npcs;
import ms.aurora.api.script.Script;
import ms.aurora.api.script.ScriptManifest;
import ms.aurora.api.wrappers.RSModel;
import ms.aurora.api.wrappers.RSNPC;
import ms.aurora.event.listeners.PaintListener;

import java.awt.*;

import static ms.aurora.api.filters.NpcFilters.NAMED;
import static ms.aurora.api.filters.NpcFilters.NOT_IN_COMBAT;
import static ms.aurora.api.util.Utilities.random;

/**
 * @author rvbiljouw
 */
@ScriptManifest(name = "GoblinKiller", shortDescription = "Kills Goblins anywhere..", author = "rvbiljouw", version = 1.0)
public class GoblinKiller extends Script implements PaintListener {

    @Override
    public int tick() {
        if (getMyPlayer().isMoving() || getMyPlayer().isInCombat() || getMyPlayer().getInteracting() != null) {
            return random(700, 800);
        } else {
           // doAttack();
            return random(600, 800);
        }
    }

    private void doAttack() {
        RSNPC goblin = Npcs.get(NAMED("Goblin"), NOT_IN_COMBAT);
        if (goblin != null) {
            goblin.applyAction("Attack");
        }
    }

    @Override
    public void onRepaint(Graphics graphics) {
        RSNPC goblin = Npcs.get(NAMED("Goblin"), NOT_IN_COMBAT);
        if (goblin != null && goblin.getModel() != null) {
            Polygon[] polys = new RSModel(goblin.getModel(), goblin.getLocalX(), goblin.getLocalY(), goblin.getTurnDirection()).getPolys();
            for(Polygon poly : polys) {
                graphics.drawPolygon(poly);
            }
        }
    }
}
