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
            Point p = ctx.projection.worldToMinimap(this.path[i].getX(), this.path[i].getY());
            if (p.x != -1 && p.y != -1)
                return this.path[i];
        }
        return null;
    }

    public RSTile getPrevious() {
        for (int i = 0; i < this.path.length; i++) {
            Point p = ctx.projection.worldToMinimap(this.path[i].getX(), this.path[i].getY());
            if (p.x != -1 && p.y != -1)
                return this.path[i];
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
        // TODO - finish method
        // Will walk to next tile in path (needs to be looped)
        return false;
    }

}
