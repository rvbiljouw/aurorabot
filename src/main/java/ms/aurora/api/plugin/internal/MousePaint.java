package ms.aurora.api.plugin.internal;

import ms.aurora.api.event.EventBus;
import ms.aurora.api.event.EventHandler;
import ms.aurora.api.event.PaintEvent;
import ms.aurora.rt3.IMouse;

import java.awt.*;

import static ms.aurora.api.Context.getClient;

/**
 * @author rvbiljouw
 */
public class MousePaint {

    @EventHandler
    public void onRepaint(PaintEvent event) {
        Graphics2D graphics = event.getGraphics();
        IMouse mouse = getClient().getMouse();
        if (mouse != null) {
            drawMouse(graphics, mouse.getRealX(), mouse.getRealY());
            graphics.drawString(mouse.getRealX() + ", " + mouse.getRealY(), 10, 10);
        }
    }

    private void drawMouse(Graphics g, int mouseX, int mouseY) {
        g.setColor(Color.YELLOW);
        g.drawLine(mouseX - 7, mouseY - 7, mouseX + 7, mouseY + 7);
        g.drawLine(mouseX + 7, mouseY - 7, mouseX - 7, mouseY + 7);
    }

}

