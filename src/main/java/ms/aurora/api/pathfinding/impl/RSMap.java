package ms.aurora.api.pathfinding.impl;

import ms.aurora.api.pathfinding.TileBasedMap;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;

/**
 * @author rvbiljouw
 */
public class RSMap implements TileBasedMap {
    public static final byte[][] CLIPPING_MASKS = new byte[4000][4000];
    private boolean[][] visited = new boolean[getWidthInTiles()][getHeightInTiles()];

    private static final int DIRECTION_NORTHWEST = 0x1;
    private static final int DIRECTION_NORTH = 0x2;
    private static final int DIRECTION_NORTHEAST = 0x4;
    private static final int DIRECTION_EAST = 0x8;
    private static final int DIRECTION_SOUTHEAST = 0x10;
    private static final int DIRECTION_SOUTH = 0x20;
    private static final int DIRECTION_SOUTHWEST = 0x40;
    private static final int DIRECTION_WEST = 0x80;
    private static final int BLOCKED = 0x100;
    private static final int INVALID = 0x200000 | 0x40000;

    private static final int WALL_NORTH_WEST = 0x1;
    private static final int WALL_NORTH = 0x2;
    private static final int WALL_NORTH_EAST = 0x4;
    private static final int WALL_EAST = 0x8;
    private static final int WALL_SOUTH_EAST = 0x10;
    private static final int WALL_SOUTH = 0x20;
    private static final int WALL_SOUTH_WEST = 0x40;
    private static final int WALL_WEST = 0x80;

    @Override
    public int getWidthInTiles() {
        return 4000;
    }

    @Override
    public int getHeightInTiles() {
        return 4000;
    }

    public byte getBlock(int x, int y) {
        return CLIPPING_MASKS[x][y];
    }

    public boolean solid(int x, int y) {
        return (blocked(x, y, DIRECTION_NORTH) &&
                blocked(x, y, DIRECTION_SOUTH) &&
                blocked(x, y, DIRECTION_EAST) &&
                blocked(x, y, DIRECTION_WEST) &&
                blocked(x, y, DIRECTION_NORTHEAST) &&
                blocked(x, y, DIRECTION_NORTHWEST) &&
                blocked(x, y, DIRECTION_SOUTHEAST) &&
                blocked(x, y, DIRECTION_SOUTHWEST)) ||
                (
                        blocked(x, y, INVALID | BLOCKED)

                ) || CLIPPING_MASKS[x][y] == -128;
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

    public boolean isWalkable(int x, int y, int x2, int y2) {
        int here = getBlock(x, y);
        int upper = Integer.MAX_VALUE;
        if (x == x2 && y - 1 == y2)
            return (y > 0 && (here & WALL_SOUTH) == 0 && (getBlock(x, y - 1) & (BLOCKED | INVALID)) == 0);
        if (x - 1 == x2 && y == y2)
            return (x > 0 && (here & WALL_WEST) == 0 && (getBlock(x - 1, y) & (BLOCKED | INVALID)) == 0);
        if (x == x2 && y + 1 == y2)
            return (y < upper && (here & WALL_NORTH) == 0 && (getBlock(x, y + 1) & (BLOCKED | INVALID)) == 0);
        if (x + 1 == x2 && y == y2)
            return (x < upper && (here & WALL_EAST) == 0 && (getBlock(x + 1, y) & (BLOCKED | INVALID)) == 0);
        if (x - 1 == x2 && y - 1 == y2)
            return (x > 0 && y > 0
                    && (here & (WALL_SOUTH_WEST | WALL_SOUTH | WALL_WEST)) == 0
                    && (getBlock(x - 1, y - 1) & (BLOCKED | INVALID)) == 0
                    && (getBlock(x, y - 1) & (BLOCKED | INVALID | WALL_WEST)) == 0 && (getBlock(
                    x - 1, y) & (BLOCKED | INVALID | WALL_SOUTH)) == 0);
        if (x - 1 == x2 && y + 1 == y2)
            return (x > 0 && y < upper
                    && (here & (WALL_NORTH_WEST | WALL_NORTH | WALL_WEST)) == 0
                    && (getBlock(x - 1, y + 1) & (BLOCKED | INVALID)) == 0
                    && (getBlock(x, y + 1) & (BLOCKED | INVALID | WALL_WEST)) == 0 && (getBlock(
                    x - 1, y) & (BLOCKED | INVALID | WALL_NORTH)) == 0);
        if (x + 1 == x2 && y - 1 == y2)
            return (x < upper && y > 0
                    && (here & (WALL_SOUTH_EAST | WALL_SOUTH | WALL_EAST)) == 0
                    && (getBlock(x + 1, y - 1) & (BLOCKED | INVALID)) == 0
                    && (getBlock(x, y - 1) & (BLOCKED | INVALID | WALL_EAST)) == 0 && (getBlock(
                    x + 1, y) & (BLOCKED | INVALID | WALL_SOUTH)) == 0);
        if (x + 1 == x2 && y + 1 == y2)
            return (x < upper && y < upper
                    && (here & (WALL_NORTH_EAST | WALL_NORTH | WALL_EAST)) == 0
                    && (getBlock(x + 1, y + 1) & (BLOCKED | INVALID)) == 0
                    && (getBlock(x, y + 1) & (BLOCKED | INVALID | WALL_EAST)) == 0 && (getBlock(
                    x + 1, y) & (BLOCKED | INVALID | WALL_NORTH)) == 0);
        return false;
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

    static {
        try {
            for (int x = 0; x < CLIPPING_MASKS.length; x++) {
                for (int y = 0; y < CLIPPING_MASKS[x].length; y++) {
                    CLIPPING_MASKS[x][y] = -128;
                }
            }

            DataInputStream in = new DataInputStream(new FileInputStream(new File("tiles.dat")));
            while (in.available() > 0 && in.readByte() == 0) {
                int rx = in.readInt();
                int ry = in.readInt();
                int len = in.readInt();
                for (int i = 0; i < len; i++) {
                    int sublen = in.readInt();
                    for (int j = 0; j < sublen; j++) {
                        int mask = in.readInt();
                        if ((mask & (BLOCKED | INVALID)) != 0) {
                            CLIPPING_MASKS[rx + i][ry + j] = -128;
                        } else {
                            CLIPPING_MASKS[rx + i][ry + j] = (byte) mask;
                        }
                    }
                }
                System.out.println("Loaded " + rx + ", " + ry);
            }
            System.out.println("Loading completed.");
            in.close();
        } catch (Exception e) {
            throw new RuntimeException("Failure loading map data.", e);
        }
    }


}