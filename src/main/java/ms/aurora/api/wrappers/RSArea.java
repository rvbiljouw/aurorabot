package ms.aurora.api.wrappers;

import ms.aurora.api.methods.Calculations;
import ms.aurora.api.methods.Players;
import ms.aurora.api.util.Utilities;

import java.awt.*;
import java.util.HashSet;

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

    public RSTile[] getAllTiles() {
        HashSet<RSTile> tiles = new HashSet<RSTile>();
        Rectangle areaBounds = area.getBounds();
        for (int x = areaBounds.x; x < (areaBounds.x + areaBounds.width); x++) {
            for (int y = areaBounds.y; y < (areaBounds.y + areaBounds.height); y++) {
                if (area.contains(x, y)) {
                    tiles.add(new RSTile(x, y));
                }
            }
        }
        return tiles.toArray(new RSTile[tiles.size()]);
    }

    public RSTile getNearestTile() {
        RSTile location = Players.getLocal().getLocation(), closest = null;
        double dist = Double.MAX_VALUE;
        for (RSTile tile: getAllTiles()) {
            if (Calculations.distance(location, tile) < dist) {
                closest = tile;
                dist = Calculations.distance(location, tile);
            }
        }
        return closest;
    }

    public RSTile getRandomTile() {
        RSTile[] tiles = getAllTiles();
        return tiles[Utilities.random(0, tiles.length - 1)];
    }

    public RSTile getCenter() {
        RSTile[] points = getAllTiles();
        int x = 0, y = 0;
        for (int i = 0; i < points.length; i++) {
            x += points[i].getX();
            y += points[i].getY();
        }
        x /= points.length;
        y /= points.length;
        return new RSTile(x, y);
    }

    public boolean contains(int x, int y) {
        return this.area.contains(x, y);
    }

    public boolean contains(RSTile tile) {
        return this.contains(tile.getX(), tile.getY());
    }

}
