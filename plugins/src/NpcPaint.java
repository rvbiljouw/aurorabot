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
        for(RSNPC npc : npcs) {
            Point loc = npc.getScreenLocation();
            graphics.drawString(String.valueOf(npc.getId()), loc.x, loc.y);
        }
    }

    private final Predicate<RSNPC> RSNPC_PREDICATE = new Predicate<RSNPC>() {
        @Override
        public boolean apply(RSNPC object) {
            return object.distance(ClientContext.get().getMyPlayer()) < 10;
        }
    };
}
