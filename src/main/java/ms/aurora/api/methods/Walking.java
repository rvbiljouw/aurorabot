package ms.aurora.api.methods;

import ms.aurora.api.pathfinding.Path;
import ms.aurora.api.pathfinding.impl.RSMapPathFinder;
import ms.aurora.api.pathfinding.impl.RSRegionPathFinder;
import ms.aurora.api.util.StatePredicate;
import ms.aurora.api.util.Utilities;
import ms.aurora.api.wrappers.Locatable;
import ms.aurora.api.wrappers.RSTile;
import ms.aurora.input.VirtualKeyboard;
import ms.aurora.input.VirtualMouse;
import ms.aurora.rt3.Player;
import org.apache.log4j.Logger;

import java.awt.*;

import static ms.aurora.api.methods.Calculations.distance;
import static ms.aurora.api.util.Utilities.sleepNoException;

/**
 * Date: 25/03/13
 * Time: 12:51
 *
 * @author A_C/Cov
 * @author Rick
 */
public final class Walking {
    private static final Logger logger = Logger.getLogger(Walking.class);
    public static final int FORWARDS = 0;
    public static final int BACKWARDS = 1;

    private static final StatePredicate WALKING() {
        return new StatePredicate() {
            @Override
            public boolean apply() {
                return Players.getLocal().isMoving();
            }
        };
    }

    private static final StatePredicate WALKING(final RSTile tile, final int distance) {
        return new StatePredicate() {
            @Override
            public boolean apply() {
                return Players.getLocal().isMoving() && distance(tile, Players.getLocal().getLocation()) > distance;
            }
        };
    }

    /**
     * Reverses an array of tiles
     *
     * @param path Path to reverse
     * @return reversed path
     */
    public static RSTile[] reversePath(RSTile[] path) {
        RSTile temp;
        for (int start = 0, end = path.length - 1; start < end; start++, end--) {
            temp = path[start];
            path[start] = path[end];
            path[end] = temp;
        }
        return path;
    }

    /**
     * Clicks a tile on the screen
     *
     * @param tile tile to click
     */
    public static boolean clickOnScreen(RSTile tile) {
        boolean success = false;
        Point screenPoint = Viewport.convert(tile);
        if (screenPoint.x != -1 && screenPoint.y != -1) {
            VirtualMouse.moveMouse(screenPoint.x, screenPoint.y);
            VirtualMouse.clickMouse(true);
            if (Utilities.sleepUntil(WALKING(), 1000)) {
                success = Utilities.sleepWhile(WALKING(tile, 3), 7500);
            }
        }
        return success;
    }

    public static RSTile getClosestOnMap(RSTile tile) {
        if (Minimap.convert(tile).x != -1) return tile;

        for (int x = -5; x < 5; x++) {
            for (int y = -5; y < 5; y++) {
                RSTile relative = new RSTile(tile.getX() + x, tile.getY() + y);
                Point minimap = Minimap.convert(relative);
                if (minimap.x != -1 && minimap.y != -1) {
                    return relative;
                }
            }
        }
        return null;
    }

    /**
     * Walks the path from one end to the other.
     *
     * @param direction direction in which to walk.
     */
    public static void traverse(RSTile[] path, int direction) {
        RSTile target = direction == FORWARDS ? path[path.length - 1] : path[0];
        if (direction == BACKWARDS) {
            path = reversePath(path);
        }

        int attemptsMade = 0;
        while (Calculations.distance(Players.getLocal().getLocation(), target) > 3
                && attemptsMade < 10 && !Thread.currentThread().isInterrupted()) {
            RSTile next = nextTile(path, 10);
            if (next != null) {
                boolean status = clickOnMap(next);
                if (!status) {
                    attemptsMade++;
                }
            } else {
                logger.info("No next tile in path to " + target);
                break;
            }
            Utilities.sleepNoException(10);
        }
        clickOnMap(target);
    }

    public static RSTile nextTile(RSTile[] path, int maxDist) {
        RSTile cur = Players.getLocal().getLocation();
        for (int i = path.length - 1; i >= 0; i--) {
            if (Calculations.distance(cur, path[i]) <= maxDist
                    && Calculations.distance(cur, path[path.length - 1]) > 3) {
                return path[i];
            }
        }
        return null;
    }

    public static boolean clickOnMap(RSTile tile) {
        Point m = Minimap.convert(tile);
        if (m.x != -1 || clickOnMap(getClosestOnMap(tile))) {
            VirtualMouse.moveMouse(m.x, m.y);
            VirtualMouse.clickMouse(true);
            Utilities.sleepUntil(WALKING(), 600);
            if (Players.getLocal().isMoving()) {
                Utilities.sleepWhile(WALKING(tile, 3), 7500);
                return true;
            }
        } else {
            logger.info("Tile is off screen " + tile);
            logger.info("Current position " + Players.getLocal().getLocation());
        }
        return false;
    }

    /**
     * Tries to walk to the destination X and Y using path finding over the mapped areas of RuneScape.
     * In case no path is found, it will attempt to walk regardless by calling walkToLocal
     *
     * @param x Destination X
     * @param y Destination Y
     */
    public static void walkTo(int x, int y) {
        logger.info("Attempting walkTo(" + x + ", " + y + ");");

        try {
            RSMapPathFinder pf = new RSMapPathFinder();
            Path path = pf.getPath(x, y, RSMapPathFinder.FULL);
            if (path != null && path.getLength() != 0) {
                final RSTile[] tiles = path.toTiles(1);
                traverse(tiles, FORWARDS);
            } else {
                throw new RuntimeException("Path not found");
            }
        } catch (Exception e) {
            logger.error("Path not found to " + x + ", " + y);
            logger.warn("Attempting local navigation to " + x + "," + y);
            walkToLocal(x, y);
        }
    }

    /**
     * Path finding within a region of 104x104 tiles.
     * This method is suitable for use in small-ish dungeons
     * or with appropriate way points.
     *
     * @param x Destination X
     * @param y Destination Y
     */
    public static void walkToLocal(int x, int y) {
        logger.info("Attempting walkToLocal(" + x + ", " + y);

        RSRegionPathFinder pf = new RSRegionPathFinder();
        Path path = pf.getPath(x, y, RSRegionPathFinder.FULL);
        if (path != null && path.getLength() != 0) {
            final RSTile[] tiles = path.toTiles(1);
            traverse(tiles, FORWARDS);
        } else {
            logger.error("Local path not found to " + x + ", " + y);
        }
    }

    /**
     * @param tile destination tile
     */
    public static void walkTo(RSTile tile) {
        walkTo(tile.getX(), tile.getY());
    }

    public static void walkToLocal(RSTile tile) {
        walkToLocal(tile.getX(), tile.getY());
    }

    /**
     * @param locatable the destination
     */
    public static void walkTo(Locatable locatable) {
        walkTo(locatable.getLocation());
    }

}
