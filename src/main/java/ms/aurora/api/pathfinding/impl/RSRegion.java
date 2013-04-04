package ms.aurora.api.pathfinding.impl;

import ms.aurora.api.pathfinding.TileBasedMap;

/**
 * Created by IntelliJ IDEA.
 * User: Johan
 * Date: 2010-feb-24
 * Time: 16:19:10
 * To change this template use File | Settings | File Templates.
 */
public class RSRegion implements TileBasedMap {

    private static final int DIRECTION_NORTH = 0x1280120;
    private static final int DIRECTION_SOUTH = 0x1280102;
    private static final int DIRECTION_EAST = 0x1280180;
    private static final int DIRECTION_WEST = 0x1280108;

    private static final int DIRECTION_NORTHEAST = 0x12801e0;
    private static final int DIRECTION_NORTHWEST = 0x1280138;
    private static final int DIRECTION_SOUTHEAST = 0x1280183;
    private static final int DIRECTION_SOUTHWEST = 0x128010e;

    private int[][] blocks;
    private boolean[][] visited = new boolean[104][104];

    public RSRegion(int[][] blocks) {
        this.blocks = blocks;
    }

    public int getWidthInTiles() {
        return 104;
    }

    public int getHeightInTiles() {
        return 104;
    }

    public void clearVisited() {
        for (int x = 0; x < getWidthInTiles(); x++) {
            for (int y = 0; y < getHeightInTiles(); y++) {
                visited[x][y] = false;
            }
        }
    }

    public boolean visited(int x, int y) {
        return visited[x][y];
    }

    public int getBlock(int x, int y) {
        return blocks[x][y];
    }

    public boolean solid(int x, int y) {
        return blocked(x, y, DIRECTION_NORTH) &&
                blocked(x, y, DIRECTION_SOUTH) &&
                blocked(x, y, DIRECTION_EAST) &&
                blocked(x, y, DIRECTION_WEST) &&
                blocked(x, y, DIRECTION_NORTHEAST) &&
                blocked(x, y, DIRECTION_NORTHWEST) &&
                blocked(x, y, DIRECTION_SOUTHEAST) &&
                blocked(x, y, DIRECTION_SOUTHWEST);
    }

    public int getDirection(int x, int y) {
        if (x == 0 && y == -1) return DIRECTION_SOUTH;
        if (x == -1 && y == 0) return DIRECTION_WEST;
        if (x == 0 && y == 1) return DIRECTION_NORTH;
        if (x == 1 && y == 0) return DIRECTION_EAST;
        if (x == -1 && y == -1) return DIRECTION_SOUTHWEST;
        if (x == -1 && y == 1) return DIRECTION_NORTHWEST;
        if (x == 1 && y == -1) return DIRECTION_SOUTHEAST;
        if (x == 1 && y == 1) return DIRECTION_NORTHEAST;
        return 0;
    }

    public boolean blocked(int x, int y, int direction) {
        return (getBlock(x, y) & direction) != 0;
    }

    public float getCost(int sx, int sy, int tx, int ty) {
        return 1;
    }

    public void pathFinderVisited(int x, int y) {
        visited[x][y] = true;
    }

}
