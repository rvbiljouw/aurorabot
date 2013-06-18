package ms.aurora.rt3;

/**
 * @author Rick
 */
public interface IGameObject {

    int getHash();

    int getX();

    int getY();

    int getZ();

    int getOrientation();

    IRenderable getModel();

}
