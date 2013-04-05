package ms.aurora.api.methods;

import ms.aurora.api.Context;
import ms.aurora.api.wrappers.RSTile;
import ms.aurora.rt3.Client;

import java.awt.*;

/**
 * Viewport related functions
 * @author Rick
 */
public final class Viewport {
    private static final Rectangle BOUNDS = new Rectangle(4, 4, 512, 334);
    private static final int[] CURVE_SIN = new int[2048];
    private static final int[] CURVE_COS = new int[2048];

    /**
     * Converts a set of coordinates to a screen position
     * @param location base tile
     * @param x offset X
     * @param z offset Y
     * @param height height
     * @return A point representing the screen coordinate, of which the x/y may be -1.
     */
    public static Point convert(RSTile location, int x, int z, int height) {
        return convert(location.getX() - x, location.getY() - z, height);
    }

    /**
     * Converts a regional tile to a screen position
     * @param tile tile
     * @return A point representing the screen coordinate, of which the x/y may be -1.
     */
    public static Point convertLocal(RSTile tile) {
        return convert(tile.getX(), tile.getY(), tile.getZ());
    }

    /*
     * Converts a world-wide tile to a regional tile and converts
     * that regional tile into a Point representing the position within the viewport.
     */
    public static Point convert(RSTile tile) {
        return convertLocal(new RSTile(tile.getX() - getClient().getBaseX(), tile.getY() - getClient().getBaseY(), tile.getZ()));
    }

    /**
     * Converts a set of local coordinates into their screen positions
     * @param aX local X coordinate
     * @param aY local Y coordinate
     * @param aHeight Height of the content on the tile.
     * @return A point representing the screen coordinate, of which the x/y may be -1.
     */
    public static Point convert(int aX, int aY, int aHeight) {
        int x = aX + 5;
        int y = aY - 11;
        if (x < 128 || y < 128 || x > 13056 || y > 13056) {
            return new Point(-1, -1);
        } else {
            int tileBaseHeight = getTileHeight(x, y, getClient().getPlane()) - aHeight;
            x -= getClient().getCameraX();
            tileBaseHeight -= getClient().getCameraZ();
            y -= getClient().getCameraY();
            int sinCurveY = CURVE_SIN[getClient().getCameraPitch()];
            int cosCurveY = CURVE_COS[getClient().getCameraPitch()];
            int sinCurveX = CURVE_SIN[getClient().getCameraYaw()];
            int cosCurveX = CURVE_COS[getClient().getCameraYaw()];
            int calculation = sinCurveX * y + cosCurveX * x >> 16;
            y = y * cosCurveX - x * sinCurveX >> 16;
            x = calculation;
            calculation = cosCurveY * tileBaseHeight - sinCurveY * y >> 16;
            y = sinCurveY * tileBaseHeight + cosCurveY * y >> 16;
            tileBaseHeight = calculation;
            if (y >= 50) {
                int screenX = ((x << 9) / y + 256);
                int screenY = ((tileBaseHeight << 9) / y + 167);
                if (BOUNDS.contains(screenX, screenY)) {
                    return new Point(((x << 9) / y + 256),
                            ((tileBaseHeight << 9) / y + 167));
                }
            }
        }
        return new Point(-1, -1);
    }

    /**
     * Calculates the tile height for a specified tile
     * @param x raw local X of the tile
     * @param y raw local Y of the tile
     * @param plane current client plane
     * @return tile height
     */
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

    /**
     * INTERNAL: Retrieves a client from the current context.
     * @return client
     */
    private static Client getClient() {
        return Context.get().getClient();
    }

    /**
     * Initializes the sin and cos curves.
     */
    static {
        for (int i = 0; i < 2048; i++) {
            CURVE_SIN[i] = (int) (65536.0 * Math.sin(i * 0.0030679615));
            CURVE_COS[i] = (int) (65536.0 * Math.cos(i * 0.0030679615));
        }
    }

    public static boolean tileOnScreen(RSTile location) {
        return BOUNDS.contains(convert(location));
    }

    public static boolean localTileOnScreen(RSTile location) {
        return BOUNDS.contains(convertLocal(location));
    }
}
