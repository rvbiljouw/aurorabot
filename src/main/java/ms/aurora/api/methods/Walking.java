package ms.aurora.api.methods;

import ms.aurora.api.pathfinding.Path;
import ms.aurora.api.pathfinding.impl.RSPathFinder;
import ms.aurora.api.util.Utilities;
import ms.aurora.api.wrappers.RSTile;
import ms.aurora.input.VirtualMouse;

import java.awt.*;

import static ms.aurora.api.methods.Calculations.distance;

/**
 * Date: 25/03/13
 * Time: 12:51
 *
 * @author A_C/Cov
 */
public class Walking {

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
    public static void clickOnMap(RSTile tile) {
        Point minimapPoint = Minimap.convert(tile.getX(), tile.getY());
        if (minimapPoint.x != -1 && minimapPoint.y != -1) {
            VirtualMouse.moveMouse(minimapPoint.x, minimapPoint.y);
            VirtualMouse.clickMouse(true);
            Utilities.sleepNoException(700);
            while (Players.getLocal().isMoving() && distance(tile, Players.getLocal().getLocation()) > 5
                    && !Thread.currentThread().isInterrupted()) {
                Utilities.sleepNoException(400);
            }
        }
    }

    public static void clickOnScreen(RSTile tile) {
        Point screenPoint = Viewport.convert(tile);
        if (screenPoint.x != -1 && screenPoint.y != -1) {
            VirtualMouse.moveMouse(screenPoint.x, screenPoint.y);
            VirtualMouse.clickMouse(true);
            Utilities.sleepNoException(700);
            while (Players.getLocal().isMoving() && distance(tile, Players.getLocal().getLocation()) > 5
                    && !Thread.currentThread().isInterrupted()) {
                Utilities.sleepNoException(400);
            }
        }
    }

    /**
     * Walks the specified path.
     *
     * @param path Path to walk.
     */
    public static void walkPath(RSTile[] path) {
        for (RSTile p : path) {
            if (distance(Players.getLocal().getLocation(), p) > 8) {
                if (distance(p, path[path.length - 1]) < distance(Players.getLocal()
                        .getLocation(), path[path.length - 1])) {
                    clickOnMap(p);
                }
            }
        }

        if (distance(Players.getLocal().getLocation(), path[path.length - 1]) > 3) {
            clickOnMap(path[path.length - 1]);
        }
    }

    public static void walkTo(int x, int y) {
        RSPathFinder pf = new RSPathFinder();
        Path path = pf.getPath(x, y, RSPathFinder.FULL);
        if (path != null && path.getLength() != 0) {
            walkPath(path.toTiles(7));
        } else {
            System.out.println("Path not found to " + x + ", " + y);
        }
    }

    public static void walkTo(RSTile tile) {
        if (distance(Players.getLocal().getLocation(), tile) <= 14) {
            clickOnMap(tile);
            return;
        }

        walkTo(tile.getX(), tile.getY());
    }

}
