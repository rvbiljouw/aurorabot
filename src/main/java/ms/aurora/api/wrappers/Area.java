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
public class Area {

    private final Polygon area;

    public Area(Tile... tiles) {
        this.area = new Polygon();
        for (Tile tile: tiles) {
            this.area.addPoint(tile.getX(), tile.getY());
        }
    }

    public Area(Tile ne, Tile sw) {
        this(new Tile[] { ne, new Tile(ne.getX(), sw.getY()), sw, new Tile(sw.getX(), ne.getY()) });
    }

    public Area(int x1, int y1, int x2, int y2) {
        this(new Tile(x1 > x2 ? x1 : x2, y1 > y2 ? y1 : y2)
                , new Tile(x1 > x2 ? x2 : x1, y1 > y2 ? y2 : y1));
    }

    /**
     * Gets all the tiles contained in the Area
     *
     * @return Tile[] containing all tiles in the array.
     */
    public Tile[] getAllTiles() {
        HashSet<Tile> tiles = new HashSet<Tile>();
        Rectangle areaBounds = area.getBounds();
        for (int x = areaBounds.x; x < (areaBounds.x + areaBounds.width); x++) {
            for (int y = areaBounds.y; y < (areaBounds.y + areaBounds.height); y++) {
                if (area.contains(x, y)) {
                    tiles.add(new Tile(x, y));
                }
            }
        }
        return tiles.toArray(new Tile[tiles.size()]);
    }

    /**
     * Gets the closest Tile in the Area to the local Player
     *
     * @return Tile cloeset to Player
     */
    public Tile getNearestTile() {
        Tile location = Players.getLocal().getLocation(), closest = null;
        double dist = Double.MAX_VALUE;
        for (Tile tile: getAllTiles()) {
            if (Calculations.distance(location, tile) < dist) {
                closest = tile;
                dist = Calculations.distance(location, tile);
            }
        }
        return closest;
    }

    /**
     * Gets a random Tile in the Area
     *
     * @return random Tile from the Area
     */
    public Tile getRandomTile() {
        Tile[] tiles = getAllTiles();
        return tiles[Utilities.random(0, tiles.length - 1)];
    }

    public Tile getCenter() {
        Tile[] points = getAllTiles();
        int x = 0, y = 0;
        for (int i = 0; i < points.length; i++) {
            x += points[i].getX();
            y += points[i].getY();
        }
        x /= points.length;
        y /= points.length;
        return new Tile(x, y);
    }

    public boolean contains(int x, int y) {
        return this.area.contains(x, y);
    }

    public boolean contains(Tile tile) {
        return this.contains(tile.getX(), tile.getY());
    }

}
