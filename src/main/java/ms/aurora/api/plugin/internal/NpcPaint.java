package ms.aurora.api.plugin.internal;

import ms.aurora.api.methods.query.impl.NpcQuery;
import ms.aurora.api.wrappers.RSNPC;
import ms.aurora.event.listeners.PaintListener;

import java.awt.*;

/**
 * @author rvbiljouw
 */
public class NpcPaint implements PaintListener {

    private final NpcQuery query = new NpcQuery().distance(10);

    @Override
    public void onRepaint(Graphics graphics) {
        RSNPC[] npcs = query.result();
        for (RSNPC npc : npcs) {
            Point loc = npc.getScreenLocation();
            String s = String.format("Name: %s | Id: %s | Anim: %s",
                    npc.getName(), npc.getId(), npc.getAnimation());
            int x = loc.x - (graphics.getFontMetrics().stringWidth(s) / 2);
            graphics.drawString(s, x, loc.y);

        }
    }

}
