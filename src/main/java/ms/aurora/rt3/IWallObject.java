package ms.aurora.rt3;

/**
 * @author tobiewarburton
 */
public interface IWallObject extends IGameObject {

    int getHash();

    int getX();

    int getY();

    int getZ();

    int getOrientation();

}
