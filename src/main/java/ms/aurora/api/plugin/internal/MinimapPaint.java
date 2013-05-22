package ms.aurora.api.plugin.internal;

import ms.aurora.api.methods.Minimap;
import ms.aurora.api.methods.Players;
import ms.aurora.api.methods.Widgets;
import ms.aurora.api.wrappers.RSWidget;
import ms.aurora.event.listeners.PaintListener;

import java.awt.*;

import static ms.aurora.api.Context.getClient;

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
        graphics.drawString("Minimap 1: " + getClient().getMinimapInt1(), x, y);
        y += graphics.getFontMetrics().getHeight();
        graphics.drawString("Minimap 2: " + getClient().getMinimapInt2(), x, y);
        y += graphics.getFontMetrics().getHeight();
        graphics.drawString("Minimap 3: " + getClient().getMinimapInt3(), x, y);
        y += graphics.getFontMetrics().getHeight();

        y += graphics.getFontMetrics().getHeight() * 2;
        RSWidget mm = Widgets.getWidget(548, 85);
        graphics.drawString("Widget Position: " + new Point(mm.getX(), mm.getY()), x, y);
        y += graphics.getFontMetrics().getHeight();
        graphics.drawString("Widget Dimensions: W:" + mm.getWidth() + " H: " + mm.getHeight(), x, y);
        y += graphics.getFontMetrics().getHeight();
        graphics.drawString("Widget Type: " + mm.getType(), x, y);
        y += graphics.getFontMetrics().getHeight() * 2;

        graphics.drawRect(mm.getX(), mm.getY(), mm.getWidth() + 25, mm.getHeight());

        Point minimapLoc = Minimap.convert(Players.getLocal().getX(), Players.getLocal().getY());
        graphics.drawOval(minimapLoc.x - 1, minimapLoc.y - 1, 3, 3);
        graphics.drawString("Player on minimap: " + minimapLoc, x, y);
    }
}
