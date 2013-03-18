import ms.aurora.api.rt3.Mouse;
import ms.aurora.event.listeners.PaintListener;

import java.awt.*;

import static ms.aurora.api.ClientContext.get;

/**
 * @author rvbiljouw
 */
public class MousePaint implements PaintListener {

    @Override
    public void onRepaint(Graphics graphics) {
        Mouse mouse = get().getClient().getMouse();
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

