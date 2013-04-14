import ms.aurora.api.methods.Npcs;
import ms.aurora.api.methods.Players;
import ms.aurora.api.script.Script;
import ms.aurora.api.script.ScriptManifest;
import ms.aurora.api.wrappers.RSNPC;
import ms.aurora.event.listeners.PaintListener;

import java.awt.*;

import static ms.aurora.api.methods.filters.NpcFilters.NAMED;
import static ms.aurora.api.methods.filters.NpcFilters.NOT_IN_COMBAT;
import static ms.aurora.api.util.Utilities.random;
import static ms.aurora.api.util.Utilities.sleepNoException;

/**
 * @author rvbiljouw
 */
@ScriptManifest(name = "GoblinKiller", shortDescription = "Kills Goblins anywhere..", author = "rvbiljouw", version = 1.0, category = "Combat")
public class GoblinKiller extends Script implements PaintListener {

    private Color fill = new Color(154, 205, 50, 100), border = new Color(50, 205, 50);


    @Override
    public int tick() {
        if (Players.getLocal().isMoving() || Players.getLocal().isInCombat() ||
                Players.getLocal().getInteracting() != null) {
            return random(200, 300);
        } else {
            doAttack();
            //this.hoverGoblin();
            return random(200, 300);
        }
    }

    private void doAttack() {
        RSNPC goblin = Npcs.get(NAMED("Goblin"), NOT_IN_COMBAT);
        if (goblin != null) {
            sleepNoException(100);
            goblin.applyAction("Attack");
            sleepNoException(200);
        }
    }

    @Override
    public void onRepaint(Graphics graphics) {

    }
}
