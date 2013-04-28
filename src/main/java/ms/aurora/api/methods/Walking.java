package ms.aurora.api.methods;

import ms.aurora.api.pathfinding.Path;
import ms.aurora.api.pathfinding.impl.RSPathFinder;
import ms.aurora.api.util.StatePredicate;
import ms.aurora.api.util.Utilities;
import ms.aurora.api.wrappers.RSTile;
import ms.aurora.input.VirtualKeyboard;
import ms.aurora.input.VirtualMouse;
import org.apache.log4j.Logger;

import java.awt.*;

import static ms.aurora.api.methods.Calculations.distance;

/**
 * Date: 25/03/13
 * Time: 12:51
 *
 * @author A_C/Cov
 */
public final class Walking {

    public static final int FORWARDS = 0;
    public static final int BACKWARDS = 1;
    private static boolean walking = false;
    private static RSTile[] currPath;

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
        Logger.getLogger(Walking.class).info("Clicking tile " + tile);
        Point minimapPoint = Minimap.convert(tile.getX(), tile.getY());
        if (minimapPoint.x != -1 && minimapPoint.y != -1) {
            VirtualMouse.moveMouse(minimapPoint.x, minimapPoint.y);
            VirtualMouse.clickMouse(true);
            Utilities.sleepNoException(700);
            Utilities.sleepWhile(WALKING(tile, 5));
            success = true;
        }
        VirtualKeyboard.releaseControl();
        return success;
    }

    /**
     * Clicks a tile on the screen
     *
     * @param tile tile to click
     */
    public static void clickOnScreen(RSTile tile) {
        Point screenPoint = Viewport.convert(tile);
        if (screenPoint.x != -1 && screenPoint.y != -1) {
            VirtualMouse.moveMouse(screenPoint.x, screenPoint.y);
            VirtualMouse.clickMouse(true);
            Utilities.sleepNoException(700);
            Utilities.sleepWhile(WALKING(tile, 5));
        }
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
                clickOnMap(p);
            }
        }

        if (distance(Players.getLocal().getLocation(), path[path.length - 1]) > 3) {
            clickOnMap(path[path.length - 1]);
        }
    }

    /**
     * Gets the next tile along the path that is over 14 away.
     *
     * @return next tile along the path else null.
     */
    public static RSTile getNext(RSTile[] path) {
        for (int i = (path.length - 1); i > -1; i--) {
            if (Calculations.distance(path[i], Players.getLocal().getLocation()) <= 14) {
                return path[i];
            }
        }
        return null;
    }

    /**
     * Gets the previous tile along the path that is over 14 away.
     *
     * @return previous tile along the path else null.
     */
    public static RSTile getPrevious(RSTile[] path) {
        for (int i = 0; i < path.length; i++) {
            if (Calculations.distance(path[i], Players.getLocal().getLocation()) <= 14) {
                return path[i];
            }
        }
        return null;
    }

    /**
     * Walks to the next tile along the path.
     *
     * @param direction direction in which to walk.
     */
    public static void step(RSTile[] path, int direction) {
        RSTile tile = null;
        switch (direction) {
            case Walking.FORWARDS:
                tile = getNext(path);
                break;
            case BACKWARDS:
                tile = getPrevious(path);
                break;
        }
        if (tile == null || Calculations.distance(tile, Players.getLocal().getLocation()) < 3) {
            return;
        }
        Walking.clickOnMap(tile);
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
                return Calculations.distance(Players.getLocal().getLocation(), target) < 3;
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
            step(path, direction);
            Utilities.sleepNoException(100);
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
        RSPathFinder pf = new RSPathFinder();
        final long time = System.currentTimeMillis();
        Path path = pf.getPath(x, y, RSPathFinder.FULL);
        final long time2 = System.currentTimeMillis();
        if (path != null && path.getLength() != 0) {
            final RSTile[] tiles = path.toTiles(3);
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

}
