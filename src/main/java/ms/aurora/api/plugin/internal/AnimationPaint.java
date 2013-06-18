package ms.aurora.api.plugin.internal;

import ms.aurora.api.methods.Players;
import ms.aurora.api.wrappers.RSPlayer;
import ms.aurora.event.EventBus;

import java.awt.*;

/**
 * @author rvbiljouw
 */
public class AnimationPaint {

    @EventBus.EventHandler
    public void onRepaint(Graphics graphics) {
        RSPlayer player = Players.getLocal();
        if (player != null) {
            Point loc = player.getScreenLocation();
            graphics.drawString(String.valueOf(player.getAnimation()),
                    loc.x, loc.y);
        }
    }
}
