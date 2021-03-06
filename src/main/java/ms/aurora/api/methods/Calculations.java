package ms.aurora.api.methods;

import ms.aurora.api.wrappers.Player;
import ms.aurora.api.wrappers.Tile;

import java.awt.*;


/**
 * @author rvbiljouw
 * @author tobiewarburton
 */
public final class Calculations {

    /**
     * Calculates the distance between two tiles.
     *
     * @param t1 tile A
     * @param t2 tile B
     * @return distance between A B
     */
    public static double distance(Tile t1, Tile t2) {
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

    /**
     * Calculates the angle to the specified tile
     *
     * @param tile the tile you wish to get the angle to
     * @return the angle to tile
     */
    public static int getAngleTo(Tile tile) {
        Player myPlayer = Players.getLocal();
        int x1 = myPlayer.getLocation().getX();
        int y1 = myPlayer.getLocation().getY();
        int x = x1 - tile.getX();
        int y = y1 - tile.getY();
        double angle = Math.toDegrees(Math.atan2(x , y));
        if(x == 0 && y > 0)
            angle = 180;
        if(x < 0 && y == 0)
            angle = 90;
        if(x == 0 && y < 0)
            angle = 0;
        if(x < 0 && y == 0)
            angle = 270;
        if(x < 0 && y > 0)
            angle+=270;
        if(x > 0 && y > 0)
            angle+=90;
        if(x < 0 && y < 0)
            angle=Math.abs(angle)-180;
        if(x > 0 && y < 0)
            angle=Math.abs(angle)+270;
        if(angle<0)
            angle=360+angle;
        if(angle>=360)
            angle-=360;
        return (int)angle;
    }


}
