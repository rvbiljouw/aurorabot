package ms.aurora.api.plugin.internal;

import ms.aurora.api.methods.tabs.Shop;
import ms.aurora.event.listeners.PaintListener;

import java.awt.*;

/**
 * Date: 18/04/13
 * Time: 13:41
 *
 * @author A_C/Cov
 */
public class ShopPaint implements PaintListener {

    @Override
    public void onRepaint(Graphics graphics) {
        Shop.ShopItem[] items = Shop.getAll();
        graphics.drawString(items.length + "", 10, 10);
        for (Shop.ShopItem item: items) {
            Rectangle r = item.getArea();
            graphics.drawRect(r.x, r.y, r.width, r.height);
        }
    }

}
