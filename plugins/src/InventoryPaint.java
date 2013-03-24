import ms.aurora.api.ClientContext;
import ms.aurora.api.tabs.Inventory;
import ms.aurora.event.listeners.PaintListener;

import java.awt.*;

/**
 * @author rvbiljouw
 */
public class InventoryPaint implements PaintListener {
    private final ClientContext ctx;

    public InventoryPaint(ClientContext ctx) {
        this.ctx = ctx;
    }

    @Override
    public void onRepaint(Graphics graphics) {
        Inventory.InventoryItem[] items = ctx.inventory.getAll();
        for(Inventory.InventoryItem item : items) {
            Rectangle loc = item.getArea();
            graphics.drawString(String.valueOf(item.getId()),
                    loc.x + 3, loc.y + 3);
        }
    }

}
