package ms.aurora.rt3;

/**
 * @author tobiewarburton
 */
public interface WallObject extends GameObject {

    int getHash();

    int getX();

    int getY();

    int getZ();

    int getOrientation();

}