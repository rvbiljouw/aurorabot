package ms.aurora.api.methods;

import ms.aurora.api.pathfinding.Path;
import ms.aurora.api.pathfinding.impl.RSPathFinder;
import ms.aurora.api.util.StatePredicate;
import ms.aurora.api.util.Utilities;
import ms.aurora.api.wrappers.Locatable;
import ms.aurora.api.wrappers.RSTile;
import ms.aurora.input.VirtualKeyboard;
import ms.aurora.input.VirtualMouse;
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
     * Clicks tile on the minimap
     */
    public static boolean clickOnMap(RSTile tile) {
        boolean success = false;
        VirtualKeyboard.holdControl();
        logger.info("Walking: " + tile);
        Point minimapPoint = Minimap.convert(tile.getX(), tile.getY());
        if (minimapPoint.x != -1 && minimapPoint.y != -1) {
            VirtualMouse.moveMouse(minimapPoint.x, minimapPoint.y);
            VirtualMouse.clickMouse(true);
            sleepNoException(500, 700);
            success = Utilities.sleepWhile(WALKING(tile, 3), 7500);
        } else {
            logger.error("Tile not on minimap: " + tile);
        }
        VirtualKeyboard.releaseControl();
        return success;
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
            if (Utilities.sleepUntil(WALKING(), 2500)) {
                success = Utilities.sleepWhile(WALKING(tile, 3), 7500);
            }
        }
        return success;
    }

    /**
     * Walks the specified path.
     *
     * @param path Path to walk.
     */
    @Deprecated
    public static void walkPath(RSTile[] path) {
        for (RSTile p : path) {
            int currentDist = (int) distance(p, path[path.length - 1]);
            int maxDist = (int) distance(Players.getLocal().getLocation(), path[path.length - 1]);
            if (currentDist <= maxDist) {
                if (!clickOnMap(p)) {

                    return;
                }
            }
        }

        if (distance(Players.getLocal().getLocation(), path[path.length - 1]) > 3) {
            clickOnMap(path[path.length - 1]);
        }
    }

    private static int getCurrentPosition(RSTile[] path) {
        int idx = -1;
        double distance = Double.MAX_VALUE;
        RSTile currentLocation = Players.getLocal().getLocation();
        for (int i = 0; i < path.length; i++) {
            double currentDistance = Calculations.distance(currentLocation, path[i]);
            if (currentDistance < distance) {
                idx = i;
                distance = currentDistance;
            }
        }
        return idx;
    }

    /**
     * Gets the next tile along the path that is over 14 away.
     *
     * @return next tile along the path else null.
     */
    public static RSTile getNext(RSTile[] path) {
        int idx = getCurrentPosition(path);
        RSTile previous = Players.getLocal().getLocation(), next = path[idx];
        double distance = Calculations.distance(previous, next);
        for (int i = idx + 1; i < path.length; i++) {
            previous = next;
            next = path[i];
            distance += Calculations.distance(previous, next);
            if (distance >= 14D) {
                return previous;
            }
        }
        return path[path.length - 1];
    }

    /**
     * Gets the previous tile along the path that is over 14 away.
     *
     * @return previous tile along the path else null.
     */
    public static RSTile getPrevious(RSTile[] path) {
        int idx = getCurrentPosition(path);
        RSTile previous = Players.getLocal().getLocation(), next = path[idx];
        double distance = Calculations.distance(previous, next);
        for (int i = idx + 1; i > 0; i--) {
            previous = next;
            next = path[i];
            distance += Calculations.distance(previous, next);
            if (distance >= 14D) {
                return previous;
            }
        }
        return path[0];
    }

    /**
     * Walks to the next tile along the path.
     *
     * @param direction direction in which to walk.
     */
    public static boolean step(RSTile[] path, int direction) {
        RSTile tile = null;
        switch (direction) {
            case Walking.FORWARDS:
                tile = getNext(path);
                break;
            case BACKWARDS:
                tile = getPrevious(path);
                break;
        }

        return tile != null && (Calculations.distance(tile, Players.getLocal().getLocation()) < 4 || Walking.clickOnMap(tile));
    }

    /**
     * Walks the path from one end to the other.
     *
     * @param direction direction in which to walk.
     */
    public static void traverse(RSTile[] path, int direction) {
        final RSTile target = direction == FORWARDS ? path[path.length - 1] : path[0];
        traverse(path, new StatePredicate() {
            @Override
            public boolean apply() {
                return Calculations.distance(Players.getLocal().getLocation(), target) <= 3;
            }
        }, direction);
    }

    /**
     * Walks the path until the a certain condition
     *
     * @param walkUntil condition to stop walking.
     * @param direction direction in which to walk.
     */
    public static void traverse(RSTile[] path, StatePredicate walkUntil, int direction) {
        while (!walkUntil.apply() && !Thread.currentThread().isInterrupted()) {
            if (!step(path, direction)) {
                break;
            }
        }
    }

    /**
     * UNSTABLE: Walks to a specific coordinate
     * It is required  that this coordinate lies in the same
     * region as the current position of the player, or it will not work.
     *
     * @param x Destination X
     * @param y Destination Y
     */
    public static void walkTo(int x, int y) {
        if (x >= 4000 || y >= 4000) {
            System.out.println("Trying to walk in unmapped area.. please make your own path.");
            return;
        }

        RSPathFinder pf = new RSPathFinder();
        Path path = pf.getPath(x, y, RSPathFinder.FULL);
        if (path != null && path.getLength() != 0) {
            final RSTile[] tiles = path.toTiles(1);
            traverse(tiles, FORWARDS); // Path's by pathfinder are always inverted.
        } else {
            System.out.println("Path not found to " + x + ", " + y);
        }
    }

    /**
     * @param tile destination tile
     */
    public static void walkTo(RSTile tile) {
        walkTo(tile.getX(), tile.getY());
    }

    /**
     * @param locatable the destination
     */
    public static void walkTo(Locatable locatable) {
        walkTo(locatable.getLocation());
    }

}
