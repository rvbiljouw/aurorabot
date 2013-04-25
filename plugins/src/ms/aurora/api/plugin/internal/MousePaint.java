package ms.aurora.api.plugin.internal;

import ms.aurora.api.Context;
import ms.aurora.event.listeners.PaintListener;
import ms.aurora.rt3.Mouse;

import java.awt.*;

/**
 * @author rvbiljouw
 */
public class MousePaint implements PaintListener {

    @Override
    public void onRepaint(Graphics graphics) {
        Mouse mouse = Context.get().getClient().getMouse();
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

