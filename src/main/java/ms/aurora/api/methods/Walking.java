package ms.aurora.api.methods;

import ms.aurora.api.Context;
import ms.aurora.api.wrappers.RSTile;
import ms.aurora.api.wrappers.RSTilePath;
import ms.aurora.input.VirtualMouse;

import java.awt.*;

/**
 * Date: 25/03/13
 * Time: 12:51
 *
 * @author A_C/Cov
 */
public class Walking {

    public static RSTile[] reversePath(RSTile... path) {
        RSTile temp;
        for(int start = 0, end = path.length -1; start < end; start++, end--){
            temp = path[start];
            path[start] = path[end];
            path[end] = temp;
        }
        return path;
    }

    public static RSTilePath createPath(RSTile[] path) {
        return new RSTilePath(Context.get(), path);
    }

    public static boolean clickTile(RSTile tile) {
        return clickTile(tile, 0, 0);
    }

    public static boolean clickTile(RSTile tile, int offsetX, int offsetY) {
        VirtualMouse mouse = Context.get().input.getMouse();
        Point point = Viewport.convert(tile, offsetX, offsetY, tile.getZ());
        mouse.moveMouse(point.x, point.y);
        mouse.clickMouse(true);
        return true;
    }

    public static boolean applyAction(RSTile tile, String action) {
       return applyAction(tile, action, 0, 0);
    }

    public static boolean applyAction(RSTile tile, String action, int offsetX, int offsetY) {
        VirtualMouse mouse = Context.get().input.getMouse();
        Point point  = Viewport.convert(tile, offsetX, offsetY, tile.getZ());
        mouse.moveMouse(point.x, point.y);
        return Menu.click(action);
    }

    public static boolean clickMap(RSTile tile) {
        VirtualMouse mouse = Context.get().input.getMouse();
        Point point  = Minimap.convert(tile);
        mouse.moveMouse(point.x, point.y);
        mouse.clickMouse(true);
        return true;
    }

}
