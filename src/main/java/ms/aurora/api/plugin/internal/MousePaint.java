package ms.aurora.api.plugin.internal;

import ms.aurora.event.listeners.PaintListener;
import ms.aurora.rt3.IMouse;

import java.awt.*;

import static ms.aurora.api.Context.getClient;

/**
 * @author rvbiljouw
 */
public class MousePaint implements PaintListener {

    @Override
    public void onRepaint(Graphics graphics) {
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

