package ms.aurora.api.methods;

import ms.aurora.api.wrappers.RSTile;
import ms.aurora.api.wrappers.RSWidget;

import java.awt.*;

import static ms.aurora.api.Context.get;

/**
 * A class for minimap related functions
 *
 * @author Rick
 */
public final class Minimap {
    private static final int MINIMAP_INTERFACE_GROUP = 548;
    private static final int MINIMAP_INTERFACE_CHILD = 85;
    private static final int[] CURVESIN = new int[2048];
    private static final int[] CURVECOS = new int[2048];

    /**
     * Converts a world coordinate to a minimap coordinate
     *
     * @param tile The tile to convert to minimap coordinate.
     * @return Point representing the position on the minimap. This point may have an x/y of -1.
     */
    public static Point convert(RSTile tile) {
        return convert(tile.getX(), tile.getY());
    }

    /**
     * Converts a world coordinate to a minimap coordinate.
     *
     * @param x world X coordinate
     * @param y world Y coordinate
     * @return Point representing the position on the minimap. This point may have an x/y of -1.
     */
    public static Point convert(int x, int y) {
        x -= get().getClient().getBaseX();
        y -= get().getClient().getBaseY();
        int calculatedX = x * 4 + 2 - Players.getLocal().getLocalX() / 32;
        int calculatedY = y * 4 + 2 - Players.getLocal().getLocalY() / 32;
        RSWidget mm = Widgets.getWidget(MINIMAP_INTERFACE_GROUP, MINIMAP_INTERFACE_CHILD);
        Rectangle minimap = new Rectangle(mm.getX(), mm.getY(), mm.getWidth() + 25, mm.getHeight());
        int angle = 0x7ff & get().getClient().getMinimapInt3() + get().getClient().getMinimapInt1();
        int actDistSq = calculatedX * calculatedX + calculatedY * calculatedY;
        int mmDist = Math.max(minimap.height / 2, minimap.width / 2) + 10;
        if (mmDist * mmDist >= actDistSq) {
            int cs = CURVESIN[angle];
            int fact = 256 + get().getClient().getMinimapInt2();
            cs = 256 * cs / fact;
            int cc = CURVECOS[angle];
            cc = 256 * cc / fact;
            int i_25_ = -(calculatedX * cs) + calculatedY * cc >> 16;
            int i_26_ = calculatedX * cc + calculatedY * cs >> 16;
            int screenX = minimap.x + (minimap.width / 2) + i_26_;
            int screenY = -i_25_ + minimap.y + (minimap.height / 2);
            return new Point(screenX, screenY);
        }

        /*RSWidget mm = Widgets.getWidget(MINIMAP_INTERFACE_GROUP, MINIMAP_INTERFACE_CHILD);

        x -= get().getClient().getBaseX();
        y -= get().getClient().getBaseY();

        final int xx = x * 4 + 2 - Players.getLocal().getLocalX() / 32;
        final int yy = y * 4 + 2 - Players.getLocal().getLocalY() / 32;

        int degree = get().getClient().getMinimapInt3() + get().getClient().getMinimapInt1() & 0x7FF;
        int dist = (int) (Math.pow(xx, 2) + Math.pow(yy, 2));

        if (dist <= 6400) {
            int sin = CURVESIN[degree];
            int cos = CURVECOS[degree];

            cos = cos * 256 / (get().getClient().getMinimapInt2() + 256);
            sin = sin * 256 / (get().getClient().getMinimapInt2() + 256);

            int mx = yy * sin + cos * xx >> 16;
            int my = sin * xx - yy * cos >> 16;
            if (dist < 2500) {

                final int sx = 18 + ((mm.getX() + mm.getHeight() / 2) + mx);
                final int sy = (mm.getY() + mm.getHeight() / 2 - 1) + my;

                return new Point(sx, sy);
            }

            final int screenx = 18 + ((mm.getX() + mm.getWidth() / 2) + mx);
            final int screeny = (mm.getY() + mm.getWidth() / 2 - 1) + my;

            return new Point(screenx, screeny);
        }*/
        return new Point(-1, -1);
    }

    /**
     * Initializes the sin and cos curves.
     */
    static {
        for (int i = 0; i < 2048; i++) {
            CURVESIN[i] = (int) (65536.0 * Math.sin(i * 0.0030679615));
            CURVECOS[i] = (int) (65536.0 * Math.cos(i * 0.0030679615));
        }
    }
}
