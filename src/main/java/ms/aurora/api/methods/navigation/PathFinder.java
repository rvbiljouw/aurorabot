package ms.aurora.api.methods.navigation;

import ms.aurora.api.Context;
import ms.aurora.api.methods.Calculations;
import ms.aurora.api.methods.Players;
import ms.aurora.api.methods.navigation.impl.TileNode;
import ms.aurora.api.wrappers.RSTile;
import ms.aurora.api.wrappers.RSTilePath;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static ms.aurora.api.methods.Calculations.distance;

/**
 * @author rvbiljouw
 */
public class PathFinder {
    private static GridNode[][] nodes = new GridNode[104][104];

    public static RSTilePath findPath(RSTile target) {
        List<GridNode> openNodes = newArrayList();
        List<GridNode> closedNodes = newArrayList();
        refreshNodes();

        GridNode targetNode = nodes
                [target.getX() - Context.get().getClient().getBaseX()]
                [target.getY() - Context.get().getClient().getBaseY()];
        GridNode startNode = nodes
                [Players.getLocal().getLocalX() >> 7]
                [Players.getLocal().getLocalY() >> 7];
        openNodes.add(startNode);
        while (!openNodes.isEmpty()) {
            GridNode current = openNodes.get(0);
            if(current.equals(targetNode)) {
                return toTilePath(targetNode);
            }

            openNodes.remove(current);
            closedNodes.add(current);

            List<GridNode> successors = getSuccessors(current);
            for(GridNode successor : successors) {
                if(closedNodes.contains(successor) || successor.isSolid()) continue;
                boolean improvement = false;

                int distance = (int)(current.getPastCost() + distance(successor.getX(), successor.getY(),
                        current.getX(), current.getY()));
                if(!openNodes.contains(successor)) {
                    openNodes.add(successor);
                    improvement = true;
                } else if(distance < current.getPastCost()) {
                    improvement = true;
                }

                if(improvement) {
                    successor.setPrev(current);
                    successor.setPastCost(distance);
                    successor.setFutureCost((int)distance(successor.getX(), successor.getY(),
                            target.getX(), target.getY()));
                }
            }
        }
        return null;
    }

    private static void refreshNodes() {
        for (int i = 0; i < 104; i++) {
            for (int j = 0; j < 104; j++) {
                nodes[i][j] = new TileNode(i, j);
            }
        }
    }

    private static List<GridNode> getSuccessors(GridNode parent) {
        List<GridNode> successors = newArrayList();
        // South
        if ((parent.getMask() & Bitmasks.W_S) == 0 && !nodes[parent.getX()][parent.getY() - 1].isSolid()) {
            successors.add(nodes[parent.getX()][parent.getY() - 1]);
        }

        // West
        if ((parent.getMask() & Bitmasks.W_W) == 0 && !nodes[parent.getX() - 1][parent.getY()].isSolid()) {
            successors.add(nodes[parent.getX() - 1][parent.getY()]);
        }

        // North
        if ((parent.getMask() & Bitmasks.W_N) == 0 && !nodes[parent.getX()][parent.getY() + 1].isSolid()) {
            successors.add(nodes[parent.getX()][parent.getY() + 1]);
        }

        // East
        if ((parent.getMask() & Bitmasks.W_E) == 0 && !nodes[parent.getX() + 1][parent.getY()].isSolid()) {
            successors.add(nodes[parent.getX() + 1][parent.getY()]);
        }

        // Southwest
        if ((parent.getMask() & Bitmasks.W_SW) == 0 && !nodes[parent.getX() - 1][parent.getY() - 1].isSolid()) {
            successors.add(nodes[parent.getX() - 1][parent.getY() - 1]);
        }

        // Northwest
        if ((parent.getMask() & Bitmasks.W_NW) == 0 && !nodes[parent.getX() - 1][parent.getY() + 1].isSolid()) {
            successors.add(nodes[parent.getX() - 1][parent.getY() + 1]);
        }

        // Southeast
        if ((parent.getMask() & Bitmasks.W_SE) == 0 && !nodes[parent.getX() + 1][parent.getY() - 1].isSolid()) {
            successors.add(nodes[parent.getX() + 1][parent.getY() - 1]);
        }

        // Northeast
        if ((parent.getMask() & Bitmasks.W_NE) == 0 && !nodes[parent.getX() + 1][parent.getY() + 1].isSolid()) {
            successors.add(nodes[parent.getX() + 1][parent.getY() + 1]);
        }
        return successors;
    }

    private static RSTilePath toTilePath(GridNode targetNode) {
        List<RSTile> pathTiles = newArrayList();
        GridNode current = targetNode;
        while(current != null) {
            pathTiles.add(new RSTile(current.getX(), current.getY()));
            current = current.getPrev();
        }
        return new RSTilePath(Context.get(), pathTiles.toArray(new RSTile[pathTiles.size()]));
    }
}
