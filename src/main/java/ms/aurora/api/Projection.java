package ms.aurora.api;

import ms.aurora.api.wrappers.RSTile;

import java.awt.*;

import static ms.aurora.api.ClientContext.get;

/**
 * @author rvbiljouw
 */
public class Projection {

    public static Point worldToScreen(RSTile tile) {
        int x = tile.getX();
        int y = tile.getY();
        int z = tile.getZ();
        if (x < 128 || y < 128 || x > 13056 || y > 13056) {
            return new Point(-1, -1);
        } else {
            int tileBaseHeight = getTileHeight(x, y, get().getClient()
                    .getPlane())
                    - z;
            x -= get().getClient().getCameraX();
            tileBaseHeight -= get().getClient().getCameraZ();
            y -= get().getClient().getCameraY();
            int sinCurveY = CURVESIN[get().getClient().getCameraPitch()];
            int cosCurveY = CURVECOS[get().getClient().getCameraPitch()];
            int sinCurveX = CURVESIN[get().getClient().getCameraYaw()];
            int cosCurveX = CURVECOS[get().getClient().getCameraYaw()];
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

    private static int getTileHeight(int x, int y, int plane) {
        int _x = x >> 7;
        int _y = y >> 7;
        if (_x < 0 || _y < 0 || _x > 103 || _y > 103)
            return 0;
        int _plane = plane;
        if (_plane < 3
                && (get().getClient().getTileSettings()[1][_x][_y] & 0x2) == 2)
            _plane++;
        int _x2 = x & 0x7f;
        int _y2 = y & 0x7f;
        int i_30_ = (((128 - _x2)
                * get().getClient().getTileHeights()[_plane][_x][_y] + get()
                .getClient().getTileHeights()[_plane][_x + 1][_y] * _x2) >> 7);
        int i_31_ = ((get().getClient().getTileHeights()[_plane][_x][_y + 1]
                * (128 - _x2) + _x2
                * get().getClient().getTileHeights()[_plane][1 + _x][_y + 1]) >> 7);
        return (128 - _y2) * i_30_ + _y2 * i_31_ >> 7;
    }

    public static boolean tileOnScreen(RSTile tile) {
        return !worldToScreen(tile).equals(new Point(-1, -1));
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
