package ms.aurora.api.methods.navigation;

import ms.aurora.rt3.Node;

/**
 * Represents a Node in the map graph
 * @author rvbiljouw
 */
public interface GridNode {

    public int getX();

    public int getY();

    public int getMask();

    public int getPastCost();

    public void setPastCost(int pastCost);

    public int getFutureCost();

    public void setFutureCost(int futureCost);

    public int getWeight();

    public void setWeight(int weight);

    public boolean isSolid();

    public GridNode getPrev();

    public void setPrev(GridNode prev);
}
