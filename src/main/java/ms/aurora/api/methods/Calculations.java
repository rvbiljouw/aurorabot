package ms.aurora.api.methods;

import ms.aurora.api.Context;
import ms.aurora.api.wrappers.RSTile;
import ms.aurora.api.wrappers.RSWidget;
import ms.aurora.rt3.Client;

import java.awt.*;


/**
 * @author no-one you know
 */
public final class Calculations {

    public static double distance(RSTile t1, RSTile t2) {
        return distance(t1.getX(), t1.getY(), t2.getX(), t2.getY());
    }

    public static double distance(Point p1, Point p2) {
        return distance(p1.x, p1.y, p2.x, p2.y);
    }

    public static double distance(int x1, int y1, int x2, int y2){
        return Math.hypot(x1 - x2, y1 - y2);
    }

    public static Point worldToScreen(RSTile location, int x, int z, int height) {
        return worldToScreen(location.getX() - x, location.getY() - z, height);
    }

    public static Point worldToScreen(RSTile tile) {
        return worldToScreen(tile.getX(), tile.getY(), tile.getZ());
    }

    public static Point worldToScreen(int aX, int aY, int aHeight) {
        int x = aX + 5;
        int y = aY - 11;
        int z = aHeight;
        if (x < 128 || y < 128 || x > 13056 || y > 13056) {
            return new Point(-1, -1);
        } else {
            int tileBaseHeight = getTileHeight(x, y, getClient().getPlane()) - z;
            x -= getClient().getCameraX();

            tileBaseHeight -= getClient().getCameraZ();
            y -= getClient().getCameraY();

            //tileBaseHeight -= getClient().getCameraY();
            //y -= getClient().getCameraZ();

            int sinCurveY = CURVESIN[getClient().getCameraPitch()];
            int cosCurveY = CURVECOS[getClient().getCameraPitch()];
            int sinCurveX = CURVESIN[getClient().getCameraYaw()];
            int cosCurveX = CURVECOS[getClient().getCameraYaw()];
            int calculation = sinCurveX * y + cosCurveX * x >> 16;
            y = y * cosCurveX - x * sinCurveX >> 16;
            x = calculation;
            calculation = cosCurveY * tileBaseHeight - sinCurveY * y >> 16;
            y = sinCurveY * tileBaseHeight + cosCurveY * y >> 16;
            tileBaseHeight = calculation;
            if (y >= 50) {
                int screenX = ((x << 9) / y + 256);
                int screenY = ((tileBaseHeight << 9) / y + 167);
                if (GAMESCREEN.contains(screenX, screenY)) {
                    return new Point(((x << 9) / y + 256),
                            ((tileBaseHeight << 9) / y + 167));
                }
            }
        }
        return new Point(-1, -1);
    }

    public static Point worldToMinimap(RSTile tile) {
        return worldToMinimap(tile.getX(), tile.getY());
    }

    public static Point worldToMinimap(int x, int y) {
        x -= getClient().getBaseX();
        y -= getClient().getBaseY();
        int calculatedX = x * 4 + 2 - Players.getLocal().getLocalX() / 32;
        int calculatedY = y * 4 + 2 - Players.getLocal().getLocalY() / 32;

        RSWidget mm = Widgets.getWidget(548, 85);
        Rectangle minimap = new Rectangle(mm.getX(), mm.getY(), mm.getWidth() + 25, mm.getHeight());

        int angle = 0x7ff & getClient().getMinimapInt3() + getClient().getMinimapInt1();
        int actDistSq = calculatedX * calculatedX + calculatedY * calculatedY;

        int mmDist = Math.max(minimap.height / 2, minimap.width / 2);
        if (mmDist * mmDist >= actDistSq) {
            int cs = CURVESIN[angle];
            int fact = 256 + getClient().getMinimapInt2();
            cs = 256 * cs / fact;
            int cc = CURVECOS[angle];
            cc = 256 * cc / fact;
            int i_25_ = -(calculatedX * cs) + calculatedY * cc >> 16;
            int i_26_ =   calculatedX * cc  + calculatedY * cs >> 16;

            int screenX = minimap.x + (minimap.width / 2) + i_26_;
            int screenY = -i_25_ + minimap.y + (minimap.height / 2);
            return new Point(screenX, screenY);
        }
        return new Point(-1, -1);//not on minimap
    }

    private static int getTileHeight(int x, int y, int plane) {
        int _x = x >> 7;
        int _y = y >> 7;
        if (_x < 0 || _y < 0 || _x > 103 || _y > 103) {
            return 0;
        }
        int _plane = plane;
        if (_plane < 3 && (getClient().getTileSettings()[1][_x][_y] & 0x2) == 2) {
            _plane++;
        }
        int _x2 = x & 0x7f;
        int _y2 = y & 0x7f;
        int i_30_ = (((128 - _x2)
                * getClient().getTileHeights()[_plane][_x][_y] +
                getClient().getTileHeights()[_plane][_x + 1][_y] * _x2) >> 7);
        int i_31_ = ((getClient().getTileHeights()[_plane][_x][_y + 1]
                * (128 - _x2) + _x2
                * getClient().getTileHeights()[_plane][1 + _x][_y + 1]) >> 7);
        return (128 - _y2) * i_30_ + _y2 * i_31_ >> 7;
    }

    public static boolean tileOnScreen(RSTile tile) {
        return !worldToScreen(tile).equals(new Point(-1, -1));
    }
    
    private static Client getClient() {
        return Context.get().getClient();
    }

    private static final int[] CURVESIN = new int[2048];
    private static final int[] CURVECOS = new int[2048];

    static {
        for (int i = 0; i < 2048; i++) {
            CURVESIN[i] = (int) (65536.0 * Math.sin(i * 0.0030679615));
            CURVECOS[i] = (int) (65536.0 * Math.cos(i * 0.0030679615));
        }
    }

    private static final Rectangle GAMESCREEN = new Rectangle(4, 4, 512, 334);
}
