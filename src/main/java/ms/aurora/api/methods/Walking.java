package ms.aurora.api.methods;

import ms.aurora.api.ClientContext;
import ms.aurora.api.wrappers.RSTile;
import ms.aurora.api.wrappers.RSTilePath;

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
        // TODO - finish method
        return false;
    }

    public boolean clickTile(RSTile tile, double offsetX, double offsetY) {
        // TODO - finish method
        return false;
    }

    public boolean applyAction(RSTile tile, String action) {
        // TODO - finish method
        return false;
    }

    public boolean applyAction(RSTile tile, String action, double offsetX, double offsetY) {
        // TODO - finish method
        return false;
    }

    public boolean clickMap(RSTile tile) {
        // TODO - finish method
        return false;
    }

}
