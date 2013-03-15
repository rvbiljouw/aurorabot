package ms.aurora.api.rt3;

/**
 * @author rvbiljouw
 */
public interface Ground {

    public GroundDecoration getGroundDecoration();

    public WallDecoration getWallDecoration();

    public WallObject getWallObject();

    public AnimableObject[] getAnimableObjects();

}
