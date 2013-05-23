package ms.aurora.api.plugin.internal;

import ms.aurora.api.methods.Npcs;
import ms.aurora.api.methods.Players;
import ms.aurora.api.methods.poc.NpcsPOC;
import ms.aurora.api.methods.poc.query.Sort;
import ms.aurora.api.util.Predicate;
import ms.aurora.api.wrappers.RSNPC;
import ms.aurora.event.listeners.PaintListener;

import java.awt.*;

/**
 * @author rvbiljouw
 */
public class NpcPaint implements PaintListener {

    @Override
    public void onRepaint(Graphics graphics) {
        //RSNPC[] npcs = Npcs.getAll(RSNPC_PREDICATE);
        RSNPC[] npcs = NpcsPOC.get().distance(10, Players.getLocal()).result();
        RSNPC monk = NpcsPOC.get().name("Monk").sort(Sort.Type.DISTANCE).single();
        for (RSNPC npc : npcs) {
            Point loc = npc.getScreenLocation();
            String s = String.format("Name: %s | Id: %s | Anim: %s",
                    npc.getName(), npc.getId(), npc.getAnimation());
            int x = loc.x - (graphics.getFontMetrics().stringWidth(s) / 2);
            graphics.drawString(s, x, loc.y);

        }
    }

    private final Predicate<RSNPC> RSNPC_PREDICATE = new Predicate<RSNPC>() {
        @Override
        public boolean apply(RSNPC object) {
            return object.distance(Players.getLocal()) < 10;
        }
    };
}
