import com.sun.org.apache.xerces.internal.dom.DOMNormalizer;
import ms.aurora.api.ClientContext;
import ms.aurora.api.Players;
import ms.aurora.api.Projection;
import ms.aurora.api.Widgets;
import ms.aurora.api.util.Utilities;
import ms.aurora.api.wrappers.RSWidget;
import ms.aurora.event.listeners.PaintListener;

import java.awt.*;

/**
 * Date: 21/03/13
 * Time: 11:06
 *
 * @author A_C/Cov
 */
public class MinimapPaint implements PaintListener {

    @Override
    public void onRepaint(Graphics graphics) {
        int x = 10, y = 10;
        graphics.drawString("Minimap 1: " + ClientContext.get().getClient().getMinimapInt1(), x, y);
        y += graphics.getFontMetrics().getHeight();
        graphics.drawString("Minimap 2: " + ClientContext.get().getClient().getMinimapInt2(), x, y);
        y += graphics.getFontMetrics().getHeight();
        graphics.drawString("Minimap 3: " + ClientContext.get().getClient().getMinimapInt3(), x, y);
        y += graphics.getFontMetrics().getHeight();

        y += graphics.getFontMetrics().getHeight() * 2;
        RSWidget mm = Widgets.getWidget(548, 85);
        graphics.drawString("Widget Position: " + new Point(mm.getX(), mm.getY()), x, y);
        y += graphics.getFontMetrics().getHeight();
        graphics.drawString("Widget Dimensions: W:" + mm.getWidth() + " H: " + mm.getHeight(), x, y);
        y += graphics.getFontMetrics().getHeight();
        graphics.drawString("Widget Type: " + mm.getType(), x, y);
        y += graphics.getFontMetrics().getHeight() * 2;

        graphics.drawRect(mm.getX(), mm.getY(), mm.getWidth() / 2, mm.getHeight() / 2);

        Point minimapLoc = Projection.worldToMinimap(Players.getLocal().getX(), Players.getLocal().getY());
        graphics.drawOval(minimapLoc.x - 1, minimapLoc.y - 1, 3, 3);
        graphics.drawString("Player on minimap: " + minimapLoc, x, y);
    }
}
