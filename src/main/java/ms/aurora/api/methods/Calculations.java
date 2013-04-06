package ms.aurora.api.methods;

import ms.aurora.api.wrappers.RSTile;

import java.awt.*;


/**
 * @author no-one you know
 */
public final class Calculations {

    /**
     * Calculates the distance between two tiles.
     *
     * @param t1 tile A
     * @param t2 tile B
     * @return distance between A B
     */
    public static double distance(RSTile t1, RSTile t2) {
        return distance(t1.getX(), t1.getY(), t2.getX(), t2.getY());
    }

    /**
     * Calculates the distance between two points
     *
     * @param p1 Point A
     * @param p2 Point B
     * @return distance between A B
     */
    public static double distance(Point p1, Point p2) {
        return distance(p1.x, p1.y, p2.x, p2.y);
    }

    /**
     * Calculates the distance between two sets of coordinates
     *
     * @param x1 X-coordinate A
     * @param y1 Y-coordinate A
     * @param x2 X-coordinate B
     * @param y2 Y-coordinate B
     * @return distance between A B
     */
    public static double distance(int x1, int y1, int x2, int y2) {
        return Math.hypot(x1 - x2, y1 - y2);
    }

}
