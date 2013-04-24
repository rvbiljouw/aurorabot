package ms.aurora.api.wrappers;

import ms.aurora.api.Context;

/**
 * @author Rick
 */
public final class RSTile {
    private int x;
    private int z;
    private int y;

    public RSTile(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public RSTile(int x, int y, int z) {
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

    public int hashCode() {
        return (this.x * this.y) >> 7;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof RSTile) {
            RSTile other = (RSTile) obj;
            return other.getX() == getX() && other.getY() == getY();
        }
        return false;
    }

    @Override
    public String toString() {
        return "[x=" + x + ",y=" + y + ",z=" + z + ']';
    }
}
