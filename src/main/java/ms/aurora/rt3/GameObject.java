package ms.aurora.rt3;

/**
 * @author Rick
 */
public interface GameObject {

    int getHash();

    int getX();

    int getY();

    int getZ();

    int getOrientation();

    Renderable getModel();

}
