package ms.aurora.physics;

import java.util.Vector;

/**
 * A simulation of an electrostatic field, in which the charges
 * decide the magnetism and thus force towards (or from) the target.
 * @author rvbiljouw
 */
public class ForceField {
    private final Vector<Potential> potentialVector = new Vector<>();

    public ForceVector getForceVector(float x, float y) {
        ForceVector vector = new ForceVector( 0.0f, 0.0f );
        for(Potential potential : potentialVector) {
            float dX = (x - potential.getX()) / 4;
            float dY = (y - potential.getY()) / 4;
            float rf = dX * dX + dY * dY;
            if(rf != 0) {
                float r = (float) Math.sqrt(rf);
                float m = potential.getCharge() / rf;
                vector.setX(vector.getX() + (m * dX / r));
                vector.setY(vector.getY() + (m * dY / r));
            }
        }
        return vector;
    }


}
