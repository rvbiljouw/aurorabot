import ms.aurora.api.ClientContext;
import ms.aurora.api.util.Predicate;
import ms.aurora.api.wrappers.RSNPC;
import ms.aurora.event.listeners.PaintListener;

import java.awt.*;

/**
 * @author rvbiljouw
 */
public class NpcPaint implements PaintListener {
    private static final Rectangle GAMESCREEN = new Rectangle(4, 4, 512, 334);

    private final ClientContext ctx;

    public NpcPaint(ClientContext ctx) {
        this.ctx = ctx;
    }

    @Override
    public void onRepaint(Graphics graphics) {
        RSNPC[] npcs = ctx.npcs.getAll(RSNPC_PREDICATE);
        for (RSNPC npc : npcs) {
            Point loc = npc.getScreenLocation();
            String s = String.format("Name: %s | Id: %s | Anim: %s | Rot: %s",
                    npc.getName(), npc.getId(), npc.getAnimation(), npc.getTurnDirection());
            int x = loc.x - (graphics.getFontMetrics().stringWidth(s) / 2);
            graphics.drawString(s, x, loc.y);
        }
    }

    private final Predicate<RSNPC> RSNPC_PREDICATE = new Predicate<RSNPC>() {
        @Override
        public boolean apply(RSNPC object) {
            return object.distance(ctx.players.getLocal()) < 10;
        }
    };
}
