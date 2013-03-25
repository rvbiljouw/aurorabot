package ms.aurora.api.methods;

import ms.aurora.api.ClientContext;
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

    private ClientContext ctx;

    public Walking(ClientContext ctx) {
        this.ctx = ctx;
    }

    public RSTile[] reversePath(RSTile... path) {
        RSTile temp;
        for(int start = 0, end = path.length -1 ; start < end; start++, end--){
            temp = path[start];
            path[start] = path[end];
            path[end] = temp;
        }
        return path;
    }

    public RSTilePath createPath(RSTile[] path, boolean reverse) {
        return new RSTilePath(this.ctx, reverse ? this.reversePath(path) : path);
    }

    public boolean clickTile(RSTile tile) {
        return this.clickTile(tile, 0, 0);
    }

    public boolean clickTile(RSTile tile, int offsetX, int offsetY) {
        VirtualMouse mouse = this.ctx.input.getMouse();
        Point point  = this.ctx.calculations.worldToScreen(tile, offsetX, offsetY, tile.getZ());
        mouse.moveMouse(point.x, point.y);
        mouse.clickMouse(true);
        return true;
    }

    public boolean applyAction(RSTile tile, String action) {
       return this.applyAction(tile, action, 0, 0);
    }

    public boolean applyAction(RSTile tile, String action, int offsetX, int offsetY) {
        VirtualMouse mouse = this.ctx.input.getMouse();
        Point point  = this.ctx.calculations.worldToScreen(tile, offsetX, offsetY, tile.getZ());
        mouse.moveMouse(point.x, point.y);
        return this.ctx.menu.click(action);
    }

    public boolean clickMap(RSTile tile) {
        VirtualMouse mouse = this.ctx.input.getMouse();
        Point point  = this.ctx.calculations.worldToMinimap(tile);
        mouse.moveMouse(point.x, point.y);
        mouse.clickMouse(true);
        return true;
    }

}
