package ms.aurora.api.pathfinding;

import ms.aurora.api.pathfinding.impl.RSPathFinder;

import java.awt.geom.Point2D;
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

    public boolean[][] getWalkables() {
        boolean[][] out = new boolean[104][104];
        for (int x = 0; x < out.length; x++) {
            for (int y = 0; y < out[x].length; y++) {
                out[x][y] = map.solid(x, y);
            }
        }
        return out;
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
        for (int x = 0; x < map.getWidthInTiles(); x++) {
            for (int y = 0; y < map.getHeightInTiles(); y++) {
                nodes[x][y] = new Node(x, y);
            }
        }
    }

    public Path findPath(int sx, int sy, int tx, int ty, int full) {
        // easy first check, if the destination is blocked, we can't get there, find an adjacent tile instead

        if (map.solid(tx, ty)) {
            if ((tx - 1) < 104 && (tx - 1) > 0 && !map.solid(tx - 1, ty)) {
                tx -= 1;
            } else if ((ty - 1) < 104 && (ty - 1) > 0 && !map.solid(tx, ty - 1)) {
                ty -= 1;
            } else if ((tx + 1) < 104 && (tx + 1) > 0 && !map.solid(tx + 1, ty)) {
                tx += 1;
            } else if ((ty + 1) < 104 && (ty + 1) > 0 && !map.solid(tx, ty + 1)) {
                ty += 1;
            }
            //System.out.println("Our target is solid... wat do");
            return findPath(sx, sy, tx, ty, full);
            //return null;
        }

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
            //System.out.println(maxDepth + " / " + maxSearchDistance + " ( " + open.size() + " open.)");
            // pull out the first node in our open list, this is determined to

            // be the most likely to be the next step based on our heuristic

            Node current = getFirstInOpen();
            //System.out.println(current.x +"," + current.y);
            if (current == nodes[tx][ty]) {
                break;
            } else if (Point2D.distance(current.x, current.y, tx, ty) < 4) {
                nodes[tx][ty].parent = current;
                break;
            }

            removeFromOpen(current);
            addToClosed(current);

            // search through all the neighbours of the current node evaluating

            // them as next steps

            for (int x = -1; x < 2; x++) {
                for (int y = -1; y < 2; y++) {
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

                    if (full == RSPathFinder.FULL) {
                        isValid = isValidLocation(sx, sy, xp, yp, map.getDirection(x, y));
                    } else {
                        isValid = isValidLocation(sx, sy, xp, yp, -1);
                    }

                    if (isValid) {
                        // the cost to get to this node is cost the current plus the movement

                        // cost to reach this node. Note that the heursitic value is only used

                        // in the sorted open list

                        float nextStepCost = current.cost + getMovementCost(current.x, current.y, xp, yp);
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
        Node target = nodes[tx][ty];
        while (target != nodes[sx][sy]) {
            path.prependStep(target.x, target.y);
            target = target.parent;
        }
        path.prependStep(sx, sy);

        // thats it, we have our path

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
        if ((!invalid) && ((sx != x) || (sy != y))) {
            if (direction != -1) {
                invalid = map.blocked(x, y, direction);
            } else {
                invalid = map.solid(x, y);
            }
        }

        return !invalid;
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
