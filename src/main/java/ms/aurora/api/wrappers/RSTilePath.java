package ms.aurora.api.wrappers;

import ms.aurora.api.Context;
import ms.aurora.api.methods.Calculations;
import ms.aurora.api.methods.Players;
import ms.aurora.api.methods.Walking;

/**
 * Date: 25/03/13
 * Time: 12:57
 *
 * @author A_C/Cov
 */
public class RSTilePath {

    private Context ctx;
    private RSTile[] path;

    public RSTilePath(Context ctx, RSTile... path) {
        this.ctx = ctx;
        this.path = path;
    }

    public RSTile getNext() {
        for (int i = (this.path.length - 1); i  > -1; i--) {
            if (Calculations.distance(path[i], Players.getLocal().getLocation()) <= 14) {
                return path[i];
            }
        }
        return null;
    }

    public RSTile getPrevious() {
        for (int i = 0; i < this.path.length; i++) {
            if (Calculations.distance(path[i], Players.getLocal().getLocation()) <= 14) {
                return path[i];
            }
        }
        return null;
    }

    public RSTile getStart() {
        return this.path[0];
    }

    public RSTile getEnd() {
        return this.path[this.path.length - 1];
    }

    public boolean step() {
        RSTile tile = this.getNext();
        if (tile == null) return false;
        if (Calculations.distance(tile, Players.getLocal().getLocation()) < 3) {
            return true; // Return true as at end of path
        }
        return !Walking.clickMap(tile); // Invert to say we haven't reached end of path/path walking failed
    }

}
