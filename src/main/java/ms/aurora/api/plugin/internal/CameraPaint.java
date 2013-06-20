package ms.aurora.api.plugin.internal;

import ms.aurora.api.event.EventBus;
import ms.aurora.api.methods.Camera;

import java.awt.*;

/**
 * Date: 24/04/13
 * Time: 08:47
 *
 * @author A_C/Cov
 */
public class CameraPaint {
    @EventBus.EventHandler
    public void onRepaint(Graphics graphics) {
        int x = 10, y = 40;
        graphics.drawString("Camera Angle: " + Camera.getAngle(), x, y);
        y += graphics.getFontMetrics().getHeight();
        graphics.drawString("Camera Pitch: " + Camera.getPitch(), x, y);
    }
}
