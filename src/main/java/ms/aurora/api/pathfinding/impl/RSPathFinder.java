package ms.aurora.api.pathfinding.impl;

import ms.aurora.api.Context;
import ms.aurora.api.methods.Calculations;
import ms.aurora.api.methods.Players;
import ms.aurora.api.pathfinding.AStarPathFinder;
import ms.aurora.api.pathfinding.ClosestHeuristic;
import ms.aurora.api.pathfinding.Path;

import java.awt.*;
import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA. User: had07josjolj Date: 2010-feb-16 Time: 09:05:48 To change this template use File |
 * Settings | File Templates.
 */
public class RSPathFinder implements Runnable {

    public static final int LAZY = 0;
    public static final int FULL = 1;

    private boolean running = true;
    private AStarPathFinder pathFinder;

    private int zoneCount = 0;

    public RSPathFinder() {
        reload();
    }

    private void reload() {
        int plane = Context.getClient().getPlane();
        RSRegion region = new RSRegion(Context.getClient().getRegions()[plane].getClippingMasks());
        pathFinder = new AStarPathFinder(region, 100, true, new ClosestHeuristic());
    }

    public Path getPath(int destX, int destY, int full) {
        reload();
        Point destPoint = getFixed(new Point(destX - Context.getClient().getBaseX(),
                destY - Context.getClient().getBaseY()));
        return getPath(Players.getLocal().getLocalX() >> 7,
                Players.getLocal().getLocalY() >> 7,
                destPoint.x,
                destPoint.y, full);
    }

    private int getZoneCount(int dest) {
        return Math.round(dest / 103) + 1;
    }

    private Point getFixed(Point point) {
        if (point.x > 103) {
            zoneCount = getZoneCount(point.x);
            point.x = 103;
        }
        if (point.y > 103) {
            zoneCount = getZoneCount(point.y);
            point.y = 103;
        }
        if (point.x < 0) {
            zoneCount = getZoneCount(+point.x);
            point.x = 1;
        }
        if (point.y < 0) {
            zoneCount = getZoneCount(+point.y);
            point.y = 1;
        }
        return point;
    }

    public int getZoneCount() {
        return zoneCount;
    }

    public Path getPath(int startX, int startY, int destX, int destY, int full) {
        return pathFinder.findPath(startX, startY, destX, destY, full);
    }

    public boolean[][] getWalkables() {
        return pathFinder.getWalkables();
    }

    public void run() {
        while (running) {

        }
    }

    public void stop() {
        running = false;
    }
}
