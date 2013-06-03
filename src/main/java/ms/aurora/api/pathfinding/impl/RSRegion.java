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

    private byte[][][] clippingMasks = new byte[3][104][104];
    private boolean[][][] visited = new boolean[3][getWidthInTiles()][getHeightInTiles()];
    private static final int DIRECTION_NORTHWEST = 0x1;
    private static final int DIRECTION_NORTH = 0x2;
    private static final int DIRECTION_NORTHEAST = 0x4;
    private static final int DIRECTION_EAST = 0x8;
    private static final int DIRECTION_SOUTHEAST = 0x10;
    private static final int DIRECTION_SOUTH = 0x20;
    private static final int DIRECTION_SOUTHWEST = 0x40;
    private static final int DIRECTION_WEST = 0x80;
    public static final int BLOCKED = 0x100;
    public static final int INVALID = 0x200000 | 0x40000;

    private static final int WALL_NORTH_WEST = 0x1;
    private static final int WALL_NORTH = 0x2;
    private static final int WALL_NORTH_EAST = 0x4;
    private static final int WALL_EAST = 0x8;
    private static final int WALL_SOUTH_EAST = 0x10;
    private static final int WALL_SOUTH = 0x20;
    private static final int WALL_SOUTH_WEST = 0x40;
    private static final int WALL_WEST = 0x80;

    public RSRegion(int plane, int[][] clippingMasks) {
        for(int i = 0; i < clippingMasks.length; i++) {
            for(int j = 0; j < clippingMasks[i].length; j++) {
                int mask = clippingMasks[i][j];
                if ((mask & (RSMap.BLOCKED | RSMap.INVALID)) != 0) {
                    this.clippingMasks[plane][i][j] = -128;
                } else {
                    this.clippingMasks[plane][i][j] = (byte) mask;
                }
            }
        }
    }

    @Override
    public int getWidthInTiles() {
        return 104;
    }

    @Override
    public int getHeightInTiles() {
        return 104;
    }

    public int getBlock(int plane, int x, int y) {
        return clippingMasks[plane][x][y];
    }
    public boolean solid(int plane, int x, int y) {
        return (blocked(plane, x, y, INVALID | BLOCKED)) || clippingMasks[plane][x][y] == -128 || (blocked(plane, x, y, DIRECTION_NORTH) &&
                blocked(plane, x, y, DIRECTION_SOUTH) &&
                blocked(plane, x, y, DIRECTION_EAST) &&
                blocked(plane, x, y, DIRECTION_WEST) &&
                blocked(plane, x, y, DIRECTION_NORTHEAST) &&
                blocked(plane, x, y, DIRECTION_NORTHWEST) &&
                blocked(plane, x, y, DIRECTION_SOUTHEAST) &&
                blocked(plane, x, y, DIRECTION_SOUTHWEST));
    }

    public int getDirection(int plane, int x, int y) {
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

    public boolean free(int plane, int x, int y) {
        return !(blocked(plane, x, y, INVALID | BLOCKED) || clippingMasks[plane][x][y] == -128 || (blocked(plane, x, y, DIRECTION_NORTH) ||
                blocked(plane, x, y, DIRECTION_SOUTH) ||
                blocked(plane, x, y, DIRECTION_EAST) ||
                blocked(plane, x, y, DIRECTION_WEST) ||
                blocked(plane, x, y, DIRECTION_NORTHEAST) ||
                blocked(plane, x, y, DIRECTION_NORTHWEST) ||
                blocked(plane, x, y, DIRECTION_SOUTHEAST) ||
                blocked(plane, x, y, DIRECTION_SOUTHWEST)));
    }

    public boolean isWalkable(int plane, int x, int y, int x2, int y2) {
        int here = getBlock(plane, x, y);
        int there = getBlock(plane, x2, y2);
        if (here == -128 || there == -128) return false;

        int upper = Integer.MAX_VALUE;
        if (x == x2 && y - 1 == y2)
            return (y > 0 && (here & WALL_SOUTH) == 0 && (getBlock(plane, x, y - 1) & (BLOCKED | INVALID)) == 0);
        if (x - 1 == x2 && y == y2)
            return (x > 0 && (here & WALL_WEST) == 0 && (getBlock(plane, x - 1, y) & (BLOCKED | INVALID)) == 0);
        if (x == x2 && y + 1 == y2)
            return (y < upper && (here & WALL_NORTH) == 0 && (getBlock(plane, x, y + 1) & (BLOCKED | INVALID)) == 0);
        if (x + 1 == x2 && y == y2)
            return (x < upper && (here & WALL_EAST) == 0 && (getBlock(plane, x + 1, y) & (BLOCKED | INVALID)) == 0);
        if (x - 1 == x2 && y - 1 == y2)
            return (x > 0 && y > 0
                    && (here & (WALL_SOUTH_WEST | WALL_SOUTH | WALL_WEST)) == 0
                    && (getBlock(plane, x - 1, y - 1) & (BLOCKED | INVALID)) == 0
                    && (getBlock(plane, x, y - 1) & (BLOCKED | INVALID | WALL_WEST)) == 0 && (getBlock(
                    plane, x - 1, y) & (BLOCKED | INVALID | WALL_SOUTH)) == 0);
        if (x - 1 == x2 && y + 1 == y2)
            return (x > 0 && y < upper
                    && (here & (WALL_NORTH_WEST | WALL_NORTH | WALL_WEST)) == 0
                    && (getBlock(plane, x - 1, y + 1) & (BLOCKED | INVALID)) == 0
                    && (getBlock(plane, x, y + 1) & (BLOCKED | INVALID | WALL_WEST)) == 0 && (getBlock(
                    plane, x - 1, y) & (BLOCKED | INVALID | WALL_NORTH)) == 0);
        if (x + 1 == x2 && y - 1 == y2)
            return (x < upper && y > 0
                    && (here & (WALL_SOUTH_EAST | WALL_SOUTH | WALL_EAST)) == 0
                    && (getBlock(plane, x + 1, y - 1) & (BLOCKED | INVALID)) == 0
                    && (getBlock(plane, x, y - 1) & (BLOCKED | INVALID | WALL_EAST)) == 0 && (getBlock(
                    plane, x + 1, y) & (BLOCKED | INVALID | WALL_SOUTH)) == 0);
        if (x + 1 == x2 && y + 1 == y2)
            return (x < upper && y < upper
                    && (here & (WALL_NORTH_EAST | WALL_NORTH | WALL_EAST)) == 0
                    && (getBlock(plane, x + 1, y + 1) & (BLOCKED | INVALID)) == 0
                    && (getBlock(plane, x, y + 1) & (BLOCKED | INVALID | WALL_EAST)) == 0 && (getBlock(
                    plane, x + 1, y) & (BLOCKED | INVALID | WALL_NORTH)) == 0);
        return false;
    }

    public boolean blocked(int plane, int x, int y, int direction) {
        return (getBlock(plane, x, y) & direction) != 0;
    }

    public float getCost(int plane, int sx, int sy, int tx, int ty) {
        int direction = getDirection(plane, Math.abs(sx - tx), Math.abs(sy - ty));
        switch (direction) {
            case DIRECTION_NORTH:
            case DIRECTION_SOUTH:
            case DIRECTION_EAST:
            case DIRECTION_WEST:
                return 1;

            default:
                return 1.4F;

        }
    }

    public void pathFinderVisited(int plane, int x, int y) {
        visited[plane][x][y] = true;
    }

}
