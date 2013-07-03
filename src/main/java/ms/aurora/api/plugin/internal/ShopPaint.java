package ms.aurora.api.plugin.internal;

import ms.aurora.api.event.EventHandler;
import ms.aurora.api.event.PaintEvent;
import ms.aurora.api.methods.tabs.Shop;
import ms.aurora.api.wrappers.WidgetItem;

import java.awt.*;

/**
 * Date: 18/04/13
 * Time: 13:41
 *
 * @author A_C/Cov
 */
public class ShopPaint {

    @EventHandler
    public void onRepaint(PaintEvent event) {
        Graphics2D graphics = event.getGraphics();
        if (Shop.isOpen()) {
            WidgetItem[] items = Shop.getAll();
            graphics.drawString(items.length + "", 10, 10);
            for (WidgetItem item: items) {
                Rectangle r = item.getArea();
                graphics.drawRect(r.x, r.y, r.width, r.height);
            }
        }
    }

}
