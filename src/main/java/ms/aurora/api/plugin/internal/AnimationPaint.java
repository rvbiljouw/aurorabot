package ms.aurora.api.plugin.internal;

import ms.aurora.api.event.EventHandler;
import ms.aurora.api.event.PaintEvent;
import ms.aurora.api.methods.Players;
import ms.aurora.api.wrappers.Player;

import java.awt.*;

/**
 * @author rvbiljouw
 */
public class AnimationPaint {

    @EventHandler
    public void onRepaint(PaintEvent event) {
        Graphics2D graphics = event.getGraphics();
        Player player = Players.getLocal();
        if (player != null) {
            Point loc = player.getScreenLocation();
            graphics.drawString(String.valueOf(player.getAnimation()),
                    loc.x, loc.y);
        }
    }
}
