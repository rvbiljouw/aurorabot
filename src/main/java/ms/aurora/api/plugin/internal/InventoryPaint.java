package ms.aurora.api.plugin.internal;

import ms.aurora.api.event.EventBus;
import ms.aurora.api.event.PaintEvent;
import ms.aurora.api.methods.tabs.Inventory;
import ms.aurora.api.wrappers.WidgetItem;

import java.awt.*;

/**
 * @author rvbiljouw
 */
public class InventoryPaint {

    @EventBus.EventHandler
    public void onRepaint(PaintEvent event) {
        Graphics2D graphics = event.getGraphics();
        WidgetItem[] items = Inventory.getAll();
        for (WidgetItem item : items) {
            Rectangle loc = item.getArea();
            graphics.drawString(String.valueOf(item.getId()),
                    loc.x + 3, loc.y + 3);
            graphics.drawRect(loc.x, loc.y, loc.width, loc.height);
        }
    }

}
