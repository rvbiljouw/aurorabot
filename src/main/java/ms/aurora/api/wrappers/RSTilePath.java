package ms.aurora.api.wrappers;

import ms.aurora.api.ClientContext;

import java.awt.*;

/**
 * Date: 25/03/13
 * Time: 12:57
 *
 * @author A_C/Cov
 */
public class RSTilePath {

    private ClientContext ctx;
    private RSTile[] path;

    public RSTilePath(ClientContext ctx, RSTile... path) {
        this.ctx = ctx;
        this.path = path;
    }

    public RSTile getNext() {
        for (int i = (this.path.length - 1); i  > -1; i--) {
            if (this.ctx.calculations.distance(path[i], this.ctx.players.getLocal().getLocation()) <= 14) {
                return path[i];
            }
        }
        return null;
    }

    public RSTile getPrevious() {
        for (int i = 0; i < this.path.length; i++) {
            if (this.ctx.calculations.distance(path[i], this.ctx.players.getLocal().getLocation()) <= 14) {
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
        if (this.ctx.calculations.distance(tile, this.ctx.players.getLocal().getLocation()) < 3) {
            return true; // Return true as at end of path
        }
        return !this.ctx.walking.clickMap(tile); // Invert to say we haven't reached end of path/path walking failed
    }

}
