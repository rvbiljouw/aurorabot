package ms.aurora.rt3;

/**
 * @author rvbiljouw
 */
public interface GameObject {

    int getHash();

    int getX();

    int getY();

    int getZ();

    int getOrientation();

    Renderable getModel();

}
