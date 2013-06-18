package ms.aurora.rt3;

/**
 * @author Rick
 */
public interface IGround {

    IGroundDecoration getGroundDecoration();

    IWallDecoration getWallDecoration();

    IWallObject getWallObject();

    IAnimableObject[] getAnimableObjects();

}
