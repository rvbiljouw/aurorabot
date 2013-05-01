package ms.aurora.api.methods;

import ms.aurora.api.wrappers.RSTile;

import java.awt.*;

import static ms.aurora.api.Context.getClient;


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


    /**
     * @param startX   the startX (0 < startX < 104)
     * @param startY   the startY (0 < startY < 104)
     * @param destX    the destX (0 < destX < 104)
     * @param destY    the destY (0 < destY < 104)
     * @param isObject if it's an object, it will find path which touches it.
     * @return
     */
    public static int getRealDistanceTo(int startX, int startY, int destX,
                                        int destY, boolean isObject) {
        startX = startX - getClient().getBaseX();
        startY = startY - getClient().getBaseY();
        destX = destX - getClient().getBaseX();
        destY = destY - getClient().getBaseY();

        if (startX > -1 && startX < 104 && startY > -1 && startY < 104
                && destX > -1 && destX < 104 && destY > -1 && destY < 104) {

            int[][] via = new int[104][104];
            int[][] cost = new int[104][104];
            int[] tileQueueX = new int[4000];
            int[] tileQueueY = new int[4000];

            for (int xx = 0; xx < 104; xx++) {
                for (int yy = 0; yy < 104; yy++) {
                    via[xx][yy] = 0;
                    cost[xx][yy] = 99999999;
                }
            }

            int curX = startX;
            int curY = startY;
            via[startX][startY] = 99;
            cost[startX][startY] = 0;
            int head = 0;
            int tail = 0;
            tileQueueX[head] = startX;
            tileQueueY[head++] = startY;
            boolean foundPath = false;
            int pathLength = tileQueueX.length;
            int blocks[][] = getClient().getRegions()[getClient()
                    .getPlane()].getClippingMasks();
            while (tail != head) {
                curX = tileQueueX[tail];
                curY = tileQueueY[tail];

                if (!isObject && curX == destX && curY == destY) {
                    foundPath = true;
                    break;
                } else if (isObject) {
                    if ((curX == destX && curY == destY + 1)
                            || (curX == destX && curY == destY - 1)
                            || (curX == destX + 1 && curY == destY)
                            || (curX == destX - 1 && curY == destY)) {
                        foundPath = true;
                        break;
                    }
                }
                tail = (tail + 1) % pathLength;

                // Big and ugly block of code
                int thisCost = cost[curX][curY] + 1;
                if (curY > 0 && via[curX][curY - 1] == 0
                        && (blocks[curX][curY - 1] & 0x1280102) == 0) {
                    tileQueueX[head] = curX;
                    tileQueueY[head] = curY - 1;
                    head = (head + 1) % pathLength;
                    via[curX][curY - 1] = 1;
                    cost[curX][curY - 1] = thisCost;
                }
                if (curX > 0 && via[curX - 1][curY] == 0
                        && (blocks[curX - 1][curY] & 0x1280108) == 0) {
                    tileQueueX[head] = curX - 1;
                    tileQueueY[head] = curY;
                    head = (head + 1) % pathLength;
                    via[curX - 1][curY] = 2;
                    cost[curX - 1][curY] = thisCost;
                }
                if (curY < 104 - 1 && via[curX][curY + 1] == 0
                        && (blocks[curX][curY + 1] & 0x1280120) == 0) {
                    tileQueueX[head] = curX;
                    tileQueueY[head] = curY + 1;
                    head = (head + 1) % pathLength;
                    via[curX][curY + 1] = 4;
                    cost[curX][curY + 1] = thisCost;
                }
                if (curX < 104 - 1 && via[curX + 1][curY] == 0
                        && (blocks[curX + 1][curY] & 0x1280180) == 0) {
                    tileQueueX[head] = curX + 1;
                    tileQueueY[head] = curY;
                    head = (head + 1) % pathLength;
                    via[curX + 1][curY] = 8;
                    cost[curX + 1][curY] = thisCost;
                }
                if (curX > 0 && curY > 0 && via[curX - 1][curY - 1] == 0
                        && (blocks[curX - 1][curY - 1] & 0x128010e) == 0
                        && (blocks[curX - 1][curY] & 0x1280108) == 0
                        && (blocks[curX][curY - 1] & 0x1280102) == 0) {
                    tileQueueX[head] = curX - 1;
                    tileQueueY[head] = curY - 1;
                    head = (head + 1) % pathLength;
                    via[curX - 1][curY - 1] = 3;
                    cost[curX - 1][curY - 1] = thisCost;
                }
                if (curX > 0 && curY < 104 - 1 && via[curX - 1][curY + 1] == 0
                        && (blocks[curX - 1][curY + 1] & 0x1280138) == 0
                        && (blocks[curX - 1][curY] & 0x1280108) == 0
                        && (blocks[curX][curY + 1] & 0x1280120) == 0) {
                    tileQueueX[head] = curX - 1;
                    tileQueueY[head] = curY + 1;
                    head = (head + 1) % pathLength;
                    via[curX - 1][curY + 1] = 6;
                    cost[curX - 1][curY + 1] = thisCost;
                }
                if (curX < 104 - 1 && curY > 0 && via[curX + 1][curY - 1] == 0
                        && (blocks[curX + 1][curY - 1] & 0x1280183) == 0
                        && (blocks[curX + 1][curY] & 0x1280180) == 0
                        && (blocks[curX][curY - 1] & 0x1280102) == 0) {
                    tileQueueX[head] = curX + 1;
                    tileQueueY[head] = curY - 1;
                    head = (head + 1) % pathLength;
                    via[curX + 1][curY - 1] = 9;
                    cost[curX + 1][curY - 1] = thisCost;
                }
                if (curX < 104 - 1 && curY < 104 - 1
                        && via[curX + 1][curY + 1] == 0
                        && (blocks[curX + 1][curY + 1] & 0x12801e0) == 0
                        && (blocks[curX + 1][curY] & 0x1280180) == 0
                        && (blocks[curX][curY + 1] & 0x1280120) == 0) {
                    tileQueueX[head] = curX + 1;
                    tileQueueY[head] = curY + 1;
                    head = (head + 1) % pathLength;
                    via[curX + 1][curY + 1] = 12;
                    cost[curX + 1][curY + 1] = thisCost;
                }
            }
            if (foundPath) {
                return cost[curX][curY];
            }
        }
        return Integer.MAX_VALUE;
    }

}
