package ms.aurora.api.wrappers;

import ms.aurora.api.Context;
import ms.aurora.api.methods.Calculations;

import static ms.aurora.api.util.Utilities.random;

/**
 * @author Rick
 */
public final class Tile {
    private int x;
    private int z;
    private int y;

    public Tile() {
        // fuk m8
    }

    public Tile(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public Tile(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getZ() {
        return z;
    }

    public void setZ(int z) {
        this.z = z;
    }

    public boolean isInRegion() {
        int minX = getX() - Context.getClient().getBaseX();
        int minY = getY() - Context.getClient().getBaseY();
        return minX >= 0 && minX < 104 && minY >= 0 && minY < 104;
    }

    public int distance(Tile other) {
        return (int) Calculations.distance(this, other);
    }

    public int distance(Locatable locatable) {
        return distance(locatable.getLocation());
    }

    public int hashCode() {
        return (this.x * this.y) >> 7;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Tile) {
            Tile other = (Tile) obj;
            return other.getX() == getX() && other.getY() == getY();
        }
        return false;
    }

    @Override
    public String toString() {
        return "[x=" + x + ",y=" + y + ",z=" + z + ']';
    }

    /**
     * Generates a randomized tile.
     *
     * @param tile       Tile to randomize
     * @param deviationX Maximum difference in X
     * @param deviationY Maximum difference in Y
     * @return deviated <b>copy</b> of the tile.
     */
    public static Tile randomize(Tile tile, int deviationX, int deviationY) {
        return new Tile(random(-deviationX, deviationX) + tile.getX(),
                random(-deviationY, deviationY) + tile.getY());
    }
}
