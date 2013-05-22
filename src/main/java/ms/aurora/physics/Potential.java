package ms.aurora.physics;

/**
 * Represents a magnetic potential in an electrostatic field
 * @author rvbiljouw
 */
public class Potential {
    private int x;
    private int y;
    private float charge;

    public Potential(int x, int y, float charge) {
        this.x = x;
        this.y = y;
        this.charge = charge;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public float getCharge() {
        return charge;
    }

}
