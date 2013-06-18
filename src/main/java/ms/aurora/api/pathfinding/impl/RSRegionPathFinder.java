package ms.aurora.api.pathfinding.impl;

import ms.aurora.api.methods.Players;
import ms.aurora.api.pathfinding.AStarPathFinder;
import ms.aurora.api.pathfinding.ClosestHeuristic;
import ms.aurora.api.pathfinding.Path;
import ms.aurora.rt3.IRegion;

import java.awt.*;

import static ms.aurora.api.Context.getClient;

/**
 * @author rvbiljouw
 */
public class RSRegionPathFinder {

    public static final int LAZY = 0;
    public static final int FULL = 1;
    private AStarPathFinder pathFinder;

    public RSRegionPathFinder() {
        reload();
    }

    private void reload() {
        IRegion current = getClient().getRegions()[getClient().getPlane()];
        pathFinder = new AStarPathFinder(new RSRegion(getClient().getPlane(), current.getClippingMasks()), 90, true, new ClosestHeuristic());
    }

    public Path getPath(int destX, int destY, int full) {
        reload();
        Point destPoint = getFixedPoint(destX, destY);
        return getPath(getClient().getPlane(), Players.getLocal().getLocalX() >> 7,
                Players.getLocal().getLocalY() >> 7,
                destPoint.x,
                destPoint.y, full);
    }

    private Point getFixedPoint(int x, int y) {
        x = x - getClient().getBaseX();
        y = y - getClient().getBaseY();

        if(x > 103) {
            x = 103;
        } else if(x < 0) {
            x = 0;
        }

        if(y > 103) {
            y = 103;
        } else if(y < 0) {
            y = 0;
        }
        return new Point(x, y);
    }

    public Path getPath(int plane, int startX, int startY, int destX, int destY, int full) {
        return pathFinder.findPath(plane, startX, startY, destX, destY, full);
    }
}
