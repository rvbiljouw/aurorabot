package ms.aurora.api.plugin.internal;

import ms.aurora.api.event.EventBus;
import ms.aurora.api.event.EventHandler;
import ms.aurora.api.event.PaintEvent;
import ms.aurora.api.methods.query.impl.NpcQuery;
import ms.aurora.api.wrappers.NPC;

import java.awt.*;

/**
 * @author rvbiljouw
 */
public class NpcPaint {

    private final NpcQuery query = new NpcQuery().onScreen();

    @EventHandler
    public void onRepaint(PaintEvent event) {
        Graphics2D graphics = event.getGraphics();
        NPC[] npcs = new NPC[0];
        try {
        npcs = query.result();
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (NPC npc : npcs) {
            Point loc = npc.getScreenLocation();
            String s = String.format("Name: %s | Id: %s | Anim: %s",
                    npc.getName(), npc.getId(), npc.getAnimation());
            int x = loc.x - (graphics.getFontMetrics().stringWidth(s) / 2);
            graphics.drawString(s, x, loc.y);

        }
    }

}
