package ms.aurora.api.pathfinding.impl;

import ms.aurora.api.methods.Players;
import ms.aurora.api.pathfinding.AStarPathFinder;
import ms.aurora.api.pathfinding.ClosestHeuristic;
import ms.aurora.api.pathfinding.Path;
import ms.aurora.rt3.Region;

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
        Region current = getClient().getRegions()[getClient().getPlane()];
        pathFinder = new AStarPathFinder(new RSRegion(current.getClippingMasks()), 104, true, new ClosestHeuristic());
    }

    public Path getPath(int destX, int destY, int full) {
        reload();
        Point destPoint = new Point(destX, destY);
        return getPath(Players.getLocal().getLocalX() >> 7,
                Players.getLocal().getLocalY() >> 7,
                destPoint.x - getClient().getBaseX(),
                destPoint.y - getClient().getBaseY(), full);
    }

    public Path getPath(int startX, int startY, int destX, int destY, int full) {
        return pathFinder.findPath(startX, startY, destX, destY, full);
    }
}
