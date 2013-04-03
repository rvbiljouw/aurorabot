package ms.aurora.api.methods.navigation.impl;

import ms.aurora.api.Context;
import ms.aurora.api.methods.navigation.Bitmasks;
import ms.aurora.api.methods.navigation.GridNode;
import ms.aurora.rt3.Node;
import ms.aurora.rt3.Region;

/**
 * @author rvbiljouw
 */
public class TileNode implements GridNode {
    private final int localX;
    private final int localY;
    private int pastCost;
    private int futureCost;
    private int weight;
    private GridNode prev;

    public TileNode(int localX, int localY) {
        this.localX = localX;
        this.localY = localY;
    }

    @Override
    public int getX() {
        return localX;
    }

    @Override
    public int getY() {
        return localY;
    }

    @Override
    public int getMask() {
        Context ctx = Context.get();
        Region[] regions = ctx.getClient().getRegions();
        Region currentRegion = regions[ctx.getClient().getPlane()];
        return currentRegion.getClippingMasks()[localX][localY];
    }

    @Override
    public int getPastCost() {
        return pastCost;
    }

    @Override
    public void setPastCost(int pastCost) {
        this.pastCost = pastCost;
    }

    @Override
    public int getFutureCost() {
        return futureCost;
    }

    @Override
    public void setFutureCost(int futureCost) {
        this.futureCost = futureCost;
    }

    @Override
    public int getWeight() {
        return weight;
    }

    @Override
    public void setWeight(int weight) {
        this.weight = weight;
    }

    @Override
    public GridNode getPrev() {
        return prev;
    }

    @Override
    public void setPrev(GridNode prev) {
        this.prev = prev;
    }

    @Override
    public boolean isSolid() {
        return (getMask() & Bitmasks.W_E) != 0
                && (getMask() & Bitmasks.W_N) != 0
                && (getMask() & Bitmasks.W_S) != 0
                && (getMask() & Bitmasks.W_W) != 0
                && (getMask() & Bitmasks.W_NE) != 0
                && (getMask() & Bitmasks.W_NW) != 0
                && (getMask() & Bitmasks.W_SE) != 0
                && (getMask() & Bitmasks.W_SW) != 0
                && (getMask() & Bitmasks.WATER) != 0
                && (getMask() & Bitmasks.BLOCKED) != 0;
    }
}
