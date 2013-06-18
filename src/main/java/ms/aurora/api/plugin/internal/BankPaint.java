package ms.aurora.api.plugin.internal;

import ms.aurora.api.methods.tabs.Bank;
import ms.aurora.api.wrappers.WidgetItem;
import ms.aurora.event.listeners.PaintListener;

import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: Cov
 * Date: 28/04/13
 * Time: 21:24
 * To change this template use File | Settings | File Templates.
 */
public class BankPaint implements PaintListener {
    @Override
    public void onRepaint(Graphics graphics) {
        if (Bank.isOpen()) {
            for (WidgetItem item: Bank.getAll()) {
                Rectangle rect = item.getArea();
                graphics.drawRect(rect.x, rect.y, rect.width, rect.height);
            }
        }
    }
}
