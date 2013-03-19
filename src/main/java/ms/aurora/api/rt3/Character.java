package ms.aurora.api.rt3;

/**
 * @author rvbiljouw
 */
public interface Character extends Renderable {

    int getLocalX();

    int getLocalY();

    int getHitsLoopCycle();

    int getAnimation();

    int getCurrentHealth();

    int getLoopCycleStatus();

    int getTurnDirection();

    String getMessage();

    int getMaxHealth();

    int getInteractingEntity();

    int getPathLength();

    int getModelHeight();
}
