package ms.aurora.api.wrappers;

import java.awt.*;

/**
 * Date: 09/04/13
 * Time: 09:45
 *
 * @author A_C/Cov
 */
public class RSArea {

    private final Polygon area;

    public RSArea(RSTile... tiles) {
        this.area = new Polygon();
        for (RSTile tile: tiles) {
            this.area.addPoint(tile.getX(), tile.getY());
        }
    }

    public RSArea(RSTile ne, RSTile sw) {
        this(ne, new RSTile(ne.getX(), sw.getY()), sw, new RSTile(sw.getX(), ne.getY()));
    }

    public RSArea(int x1, int y1, int x2, int y2) {
        this(new RSTile(x1 > x2 ? x1 : x2, y1 > y2 ? y1 : y2)
                , new RSTile(x1 > x2 ? x2 : x1, y1 > y2 ? y2 : y1));
    }

    public boolean contains(int x, int y) {
        return this.area.contains(x, y);
    }

    public boolean contains(RSTile tile) {
        return this.contains(tile.getX(), tile.getY());
    }

}
