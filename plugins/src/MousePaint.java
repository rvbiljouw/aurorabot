import ms.aurora.api.Context;
import ms.aurora.rt3.Mouse;
import ms.aurora.event.listeners.PaintListener;

import java.awt.*;

/**
 * @author rvbiljouw
 */
public class MousePaint implements PaintListener {

    @Override
    public void onRepaint(Graphics graphics) {
        Mouse mouse = Context.get().getClient().getMouse();
        if(mouse != null) {
            drawMouse(graphics, mouse.getMouseX(),  mouse.getMouseY());
            graphics.drawString(mouse.getMouseX() +", " + mouse.getMouseY(), 10, 10);
        }
    }

    private void drawMouse(Graphics g, int mouseX, int mouseY) {
        g.setColor(Color.YELLOW);
        g.drawLine(mouseX - 7, mouseY - 7, mouseX + 7, mouseY + 7);
        g.drawLine(mouseX + 7, mouseY - 7, mouseX - 7, mouseY + 7);
    }

}

