package ms.aurora.api.rt3;

/**
 * @author rvbiljouw
 */
public interface Character extends Renderable {

    public int getLocalX();

    public int getLocalY();

    public int getHitsLoopCycle();

    public int getAnimation();

    public int getCurrentHealth();

    public int getLoopCycleStatus();

    public int getTurnDirection();

    public String getMessage();

    public int getMaxHealth();

    public int getInteractingEntity();

}
