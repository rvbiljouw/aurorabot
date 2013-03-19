import ms.aurora.api.ClientContext;
import ms.aurora.api.Npcs;
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
        RSNPC[] npcs = Npcs.getAll(RSNPC_PREDICATE);
        int x = 10, y = 10;
        for(RSNPC npc : npcs) {
            graphics.drawString(String.format("Npc: %s | Id: %s | Location: %s", npc.getName(), npc.getId(), npc.getLocation()), x, y);
            y += graphics.getFontMetrics().getHeight();
            //Point loc = npc.getScreenLocation();
            //graphics.drawString(String.valueOf(npc.getId()), loc.x, loc.y);
        }
    }

    private final Predicate<RSNPC> RSNPC_PREDICATE = new Predicate<RSNPC>() {
        @Override
        public boolean apply(RSNPC object) {
            return object.distance(ClientContext.get().getMyPlayer()) < 10;
        }
    };
}
