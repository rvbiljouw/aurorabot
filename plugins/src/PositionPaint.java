import ms.aurora.api.ClientContext;
import ms.aurora.api.wrappers.RSPlayer;
import ms.aurora.event.listeners.PaintListener;

import java.awt.*;

/**
 * @author rvbiljouw
 */
public class PositionPaint implements PaintListener {
    private final ClientContext ctx;

    public PositionPaint(ClientContext ctx) {
        this.ctx = ctx;
    }

    @Override
    public void onRepaint(Graphics graphics) {
        RSPlayer player = ctx.players.getLocal();
        if (player != null) {
            Point loc = player.getScreenLocation();
            graphics.drawString(player.getLocation().toString(),
                    loc.x, loc.y);
            Point minimapLoc = ctx.projection.worldToMinimap(player.getLocalX(), player.getLocalY());
            graphics.drawOval(minimapLoc.x - 1, minimapLoc.y - 1, 3, 3);
        }
    }
}
