package ms.aurora.rt3;

/**
 * @author Rick
 */
public interface Ground {

    GroundDecoration getGroundDecoration();

    WallDecoration getWallDecoration();

    WallObject getWallObject();

    AnimableObject[] getAnimableObjects();

}
