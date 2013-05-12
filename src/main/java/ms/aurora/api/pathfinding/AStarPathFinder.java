package ms.aurora.api.pathfinding;

import ms.aurora.api.Context;
import ms.aurora.api.pathfinding.impl.RSMapPathFinder;
import ms.aurora.api.pathfinding.impl.RSRegion;
import ms.aurora.api.script.ScriptState;

import java.util.ArrayList;

/**
 * Created by IntelliJ IDEA.
 * User: Johan
 * Date: 2010-feb-24
 * Time: 16:16:45
 * To change this template use File | Settings | File Templates.
 */
public class AStarPathFinder {

    private ArrayList closed = new ArrayList();
    private SortedList open = new SortedList();
    int stackDepth = 0;

    private TileBasedMap map;

    private int maxSearchDistance;


    private Node[][] nodes;

    private boolean allowDiagMovement;

    private AStarHeuristic heuristic;

    /**
     * Create a path finder with the default heuristic - closest to target.
     *
     * @param map               The map to be searched
     * @param maxSearchDistance The maximum depth we'll search before giving up
     * @param allowDiagMovement True if the search should try diaganol movement
     */
    public AStarPathFinder(TileBasedMap map, int maxSearchDistance, boolean allowDiagMovement) {
        this(map, maxSearchDistance, allowDiagMovement, new ClosestHeuristic());
    }

    /**
     * Create a path finder
     *
     * @param heuristic         The heuristic used to determine the search order of the map
     * @param map               The map to be searched
     * @param maxSearchDistance The maximum depth we'll search before giving up
     * @param allowDiagMovement True if the search should try diaganol movement
     */
    public AStarPathFinder(TileBasedMap map, int maxSearchDistance,
                           boolean allowDiagMovement, AStarHeuristic heuristic) {
        this.heuristic = heuristic;
        this.map = map;
        this.maxSearchDistance = maxSearchDistance;
        this.allowDiagMovement = allowDiagMovement;
        nodes = new Node[map.getWidthInTiles()][map.getHeightInTiles()];
        stackDepth = 0;
    }

    public Path findPath(int sx, int sy, int tx, int ty, int full) {
        if (Context.get().getSession().getScriptManager().getState() != ScriptState.RUNNING) {
            System.out.println("Stack dropped...");
            for (StackTraceElement element : Thread.currentThread().getStackTrace()) {
                System.out.println(element.getClassName() + "." + element.getMethodName() + " " + element.getLineNumber());
            }
            return null;
        }


        if (map.solid(sx, sy)) {
            System.out.println("Starting tile has a blockade..");
            for (int x = -3; x <= 3; x++) {
                for (int y = -3; y <= 3; y++) {
                    int mX = sx + x;
                    int mY = sy + y;

                    if (mX < map.getWidthInTiles() && mX > 0 && mY < map.getHeightInTiles() && mY > 0) {
                        if (!map.solid(mX, mY)) {
                            System.out.println("Picking " + mX + ","+ mY + " as a starting tile.");
                            return findPath(mX, mY, tx, ty, full);
                        }
                    }
                }
            }
        }

        // easy first check, if the destination is blocked, we can't get there, find an adjacent tile instead
        if (map.solid(tx, ty)) {
            System.out.println("Destination tile has a blockade.. ( " + tx + ", " + ty + " )");
            for (int x = -3; x <= 3; x++) {
                for (int y = -3; y <= 3; y++) {
                    int mX = tx + x;
                    int mY = ty + y;

                    if (mX < map.getWidthInTiles() && mX > 0 && mY < map.getHeightInTiles() && mY > 0) {
                        if (!map.solid(mX, mY)) {
                            System.out.println("Picking " + mX + ","+ mY + " as a destination tile.");
                            return findPath(sx, sy, mX, mY, full);
                        }
                    }
                }
            }
        }

        if (!(sx < map.getWidthInTiles() && sx > 0 && sy < map.getHeightInTiles() && sy > 0)) {
            return null;
        }
        nodes[sx][sy] = new Node((short) sx, (short) sy);
        nodes[tx][ty] = new Node((short) tx, (short) ty);

        // initial state for A*. The closed group is empty. Only the starting

        // tile is in the open list and it'e're already there
        nodes[sx][sy].cost = 0;
        nodes[sx][sy].depth = 0;
        closed.clear();
        open.clear();
        open.add(nodes[sx][sy]);

        nodes[tx][ty].parent = null;

        // while we haven'n't exceeded our max search depth
        //System.out.println("Starting the search.");
        int maxDepth = 0;
        while ((maxDepth < maxSearchDistance) && (open.size() != 0)) {
            if (Context.get().getSession().getScriptManager().getState() != ScriptState.RUNNING) {
                break;
            }

            // pull out the first node in our open list, this is determined to
            // be the most likely to be the next step based on our heuristic

            Node current = getFirstInOpen();
            //System.out.println(current.x +"," + current.y);
            if (current == nodes[tx][ty]) {
                break;
            }

            removeFromOpen(current);
            addToClosed(current);

            // search through all the neighbours of the current node evaluating

            // them as next steps

            for (int x = -1; x < 2; x++) {
                for (int y = -1; y < 2; y++) {
                    try {
                        // not a neighbour, its the current tile
                        if ((x == 0) && (y == 0)) {
                            continue;
                        }

                        // if we're not allowing diaganol movement then only

                        // one of x or y can be set

                        if (!allowDiagMovement) {
                            if ((x != 0) && (y != 0)) {
                                continue;
                            }
                        }

                        // determine the location of the neighbour and evaluate it

                        int xp = x + current.x;
                        int yp = y + current.y;
                        boolean isValid;

                        if (full == RSMapPathFinder.FULL) {
                            isValid = isValidLocation(current.x, current.y, xp, yp, map.getDirection(x, y));
                        } else {
                            isValid = isValidLocation(current.x, current.y, xp, yp, -1);
                        }

                        if (isValid) {
                            // the cost to get to this node is cost the current plus the movement

                            // cost to reach this node. Note that the heursitic value is only used

                            // in the sorted open list

                            float nextStepCost = current.cost + getMovementCost(current.x, current.y, xp, yp);
                            if (nodes[xp][yp] == null) {
                                nodes[xp][yp] = new Node((short) xp, (short) yp);
                            }
                            Node neighbour = nodes[xp][yp];
                            map.pathFinderVisited(xp, yp);

                            // if the new cost we've determined for this node is lower than

                            // it has been previously makes sure the node hasn'e've
                            // determined that there might have been a better path to get to

                            // this node so it needs to be re-evaluated

                            if (nextStepCost < neighbour.cost) {
                                if (inOpenList(neighbour)) {
                                    removeFromOpen(neighbour);
                                }
                                if (inClosedList(neighbour)) {
                                    removeFromClosed(neighbour);
                                }
                            }

                            // if the node hasn't already been processed and discarded then

                            // reset it's cost to our current cost and add it as a next possible

                            // step (i.e. to the open list)

                            if (!inOpenList(neighbour) && !(inClosedList(neighbour))) {
                                neighbour.cost = nextStepCost;
                                neighbour.heuristic = getHeuristicCost(xp, yp, tx, ty);
                                maxDepth = Math.max(maxDepth, neighbour.setParent(current));
                                addToOpen(neighbour);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        // since we'e've processData out of search
        // there was no path. Just return null

        if (nodes[tx][ty].parent == null) {
            return null;
        }

        // At this point we've definitely found a path so we can uses the parent

        // references of the nodes to find out way from the target location back

        // to the start recording the nodes on the way.

        Path path = new Path();
        if (map instanceof RSRegion) {
            path = new Path(true);
        }

        Node target = nodes[tx][ty];
        while (target != nodes[sx][sy]) {
            path.prependStep(target.x, target.y);
            target = target.parent;
        }
        path.prependStep(sx, sy);

        // deallocate all the bullshit
        for (int i = 0; i < nodes.length; i++) {
            for (int j = 0; j < nodes[i].length; j++) {
                if (nodes[i][j] != null) {
                    nodes[i][j].parent = null; // unlink the nodes..
                }
                nodes[i][j] = null;
            }
            nodes[i] = null;
        }
        nodes = null;

        // thats it, we have our path
        closed.clear();
        open.clear();
        System.gc();
        return path;
    }

    /**
     * Get the first element from the open list. This is the next
     * one to be searched.
     *
     * @return The first element in the open list
     */
    protected Node getFirstInOpen() {
        return (Node) open.first();
    }

    /**
     * Add a node to the open list
     *
     * @param node The node to be added to the open list
     */
    protected void addToOpen(Node node) {
        open.add(node);
    }

    /**
     * Check if a node is in the open list
     *
     * @param node The node to check for
     * @return True if the node given is in the open list
     */
    protected boolean inOpenList(Node node) {
        return open.contains(node);
    }

    /**
     * Remove a node from the open list
     *
     * @param node The node to remove from the open list
     */
    protected void removeFromOpen(Node node) {
        open.remove(node);
    }

    /**
     * Add a node to the closed list
     *
     * @param node The node to add to the closed list
     */
    protected void addToClosed(Node node) {
        closed.add(node);
    }

    /**
     * Check if the node supplied is in the closed list
     *
     * @param node The node to search for
     * @return True if the node specified is in the closed list
     */
    protected boolean inClosedList(Node node) {
        return closed.contains(node);
    }

    /**
     * Remove a node from the closed list
     *
     * @param node The node to remove from the closed list
     */
    protected void removeFromClosed(Node node) {
        closed.remove(node);
    }

    /**
     * Check if a given location is valid for the supplied mover
     *
     * @param sx The starting x coordinate
     * @param sy The starting y coordinate
     * @param x  The x coordinate of the location to check
     * @param y  The y coordinate of the location to check
     * @return True if the location is valid for the given mover
     */
    protected boolean isValidLocation(int sx, int sy, int x, int y, int direction) {

        boolean invalid = (x < 0) || (y < 0) || (x >= map.getWidthInTiles()) || (y >= map.getHeightInTiles());
        if (map.getBlock(x, y) == -128 || map.solid(x, y) || invalid) return false;
        return map.isWalkable(sx, sy, x, y);
    }

    /**
     * Get the cost to move through a given location
     *
     * @param sx The x coordinate of the tile whose cost is being determined
     * @param sy The y coordiante of the tile whose cost is being determined
     * @param tx The x coordinate of the target location
     * @param ty The y coordinate of the target location
     * @return The cost of movement through the given tile
     */
    public float getMovementCost(int sx, int sy, int tx, int ty) {
        return map.getCost(sx, sy, tx, ty);
    }

    /**
     * Get the heuristic cost for the given location. This determines in which
     * order the locations are processed.
     *
     * @param x  The x coordinate of the tile whose cost is being determined
     * @param y  The y coordiante of the tile whose cost is being determined
     * @param tx The x coordinate of the target location
     * @param ty The y coordinate of the target location
     * @return The heuristic cost assigned to the tile
     */
    public float getHeuristicCost(int x, int y, int tx, int ty) {
        return heuristic.getCost(map, x, y, tx, ty);
    }

}
