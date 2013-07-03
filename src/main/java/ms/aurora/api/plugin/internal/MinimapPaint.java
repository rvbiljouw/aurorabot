package ms.aurora.api.plugin.internal;

import ms.aurora.api.event.EventHandler;
import ms.aurora.api.event.PaintEvent;
import ms.aurora.api.methods.Minimap;
import ms.aurora.api.methods.Players;
import ms.aurora.api.methods.Viewport;
import ms.aurora.api.methods.Widgets;
import ms.aurora.api.pathfinding.impl.RSRegion;
import ms.aurora.api.wrappers.Widget;
import ms.aurora.rt3.IRegion;

import java.awt.*;

import static ms.aurora.api.Context.getClient;

/**
 * Date: 21/03/13
 * Time: 11:06
 *
 * @author A_C/Cov
 */
public class MinimapPaint {

    @EventHandler
    public void onRepaint(PaintEvent event) {
        Graphics2D graphics = event.getGraphics();
        int x = 10, y = 10;
        graphics.drawString("Minimap 1: " + getClient().getMinimapInt1(), x, y);
        y += graphics.getFontMetrics().getHeight();
        graphics.drawString("Minimap 2: " + getClient().getMinimapInt2(), x, y);
        y += graphics.getFontMetrics().getHeight();
        graphics.drawString("Minimap 3: " + getClient().getMinimapInt3(), x, y);
        y += graphics.getFontMetrics().getHeight();

        y += graphics.getFontMetrics().getHeight() * 2;
        Widget mm = Widgets.getWidget(548, 85);
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


        IRegion r = getClient().getRegions()[getClient().getPlane()];
        RSRegion region = new RSRegion(getClient().getPlane(), r.getClippingMasks());
        int lx = (Players.getLocal().getLocalX() >> 7);
        int ly = (Players.getLocal().getLocalY() >> 7);
        for(int i = lx - 10; i < lx + 10; i++) {
            for(int j = ly - 10; j < ly + 10; j++) {
                if(i > 0 && i < 104 && j > 0 && j < 104) {
                    boolean n = region.blocked(getClient().getPlane(), i, j, RSRegion.DIRECTION_NORTH);
                    boolean e = region.blocked(getClient().getPlane(), i, j, RSRegion.DIRECTION_EAST);
                    boolean s = region.blocked(getClient().getPlane(), i, j, RSRegion.DIRECTION_SOUTH);
                    boolean w = region.blocked(getClient().getPlane(), i, j, RSRegion.DIRECTION_WEST);
                    boolean invalid = region.getBlock(getClient().getPlane(), i, j) == -128;

                    Point pos = Viewport.convert((int)((i * 128) + 0.5D), (int)((j * 128) + 0.5D), Players.getLocal().getHeight());
                    graphics.setColor(n ? Color.RED : Color.GREEN);
                    graphics.drawString("n", pos.x - 7, pos.y);
                    graphics.setColor(s ? Color.RED : Color.GREEN);
                    graphics.drawString("s", pos.x, pos.y);
                    graphics.setColor(e ? Color.RED : Color.GREEN);
                    graphics.drawString("e", pos.x + 7, pos.y);
                    graphics.setColor(w ? Color.RED : Color.GREEN);
                    graphics.drawString("w", pos.x + 14, pos.y);
                    graphics.setColor(invalid ? Color.RED : Color.GREEN);
                    graphics.drawString("i" ,pos.x + 21, pos.y);

                }
            }
        }
    }
}
