package ms.aurora.rt3;

/**
 * @author rvbiljouw
 */
public interface Ground {

    GroundDecoration getGroundDecoration();

    WallDecoration getWallDecoration();

    WallObject getWallObject();

    AnimableObject[] getAnimableObjects();

}
