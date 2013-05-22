package ms.aurora.api.methods;

import ms.aurora.api.util.ArrayUtils;
import ms.aurora.api.util.Predicate;
import ms.aurora.api.wrappers.RSObject;
import ms.aurora.rt3.AnimableObject;
import ms.aurora.rt3.Client;
import ms.aurora.rt3.Ground;
import ms.aurora.rt3.WorldController;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static ms.aurora.api.Context.getClient;

/**
 * Object related methods
 *
 * @author Rick
 */
public final class Objects {

    /**
     * a method which gets the closest {@link RSObject} in the current region which satisfy the {@link Predicate}
     *
     * @param predicates the list of {@link Predicate} which needs to be satisfied
     * @return the closest {@link RSObject} which satisfies the given {@link Predicate} else null
     * @see RSObject#distance(ms.aurora.api.wrappers.Locatable)
     * @see Predicate
     */
    public static RSObject get(final Predicate<RSObject>... predicates) {
        return getClosest(ArrayUtils.filter(getAll(), predicates).toArray(new RSObject[0]));
    }

    /**
     * a method which gets all the {@link RSObject} in the current region which satisfy the given {@link Predicate}
     *
     * @param predicates the {@link Predicate} which needs to be satisfied
     * @return an array containing all of the {@link RSObject} which satisfy the predicate
     * @see Predicate
     */
    public static RSObject[] getAll(final Predicate<RSObject>... predicates) {
        return ArrayUtils.filter(getAll(), predicates).toArray(new RSObject[0]);
    }

    /**
     * a method which gets all the {@link RSObject} in the current region
     *
     * @return an array of {@link RSObject} in the current region
     */
    public static RSObject[] getAll() {
        List<RSObject> objects = new ArrayList<RSObject>();
        for (int x = 0; x < 104; x++) {
            for (int y = 0; y < 104; y++) {
                objects.addAll(getObjectsAtLocal(x, y));
            }
        }
        return objects.toArray(new RSObject[objects.size()]);
    }

    /**
     * Gets the closest object out of an array of objects
     *
     * @param objects Array of objects
     * @return Closest object
     */
    private static RSObject getClosest(RSObject[] objects) {
        RSObject closest = null;
        int closestDistance = 9999;
        for (RSObject object : objects) {
            int distance = object.distance(Players.getLocal());
            if (distance < closestDistance) {
                closestDistance = distance;
                closest = object;
            }
        }
        return closest;
    }

    /**
     * Gets all object at a specified tile
     * @param x tile X
     * @param y tile Y
     * @return collection of objects
     */
    public static Collection<RSObject> getObjectsAt(int x, int y) {
        return getObjectsAtLocal(x - getClient().getBaseX(), y - getClient().getBaseY());
    }

    /**
     * Gets all the objects at specified x/y
     *
     * @param x local X coordinate
     * @param y local Y coordinate
     * @return collection of objects at the specified tile
     */
    public static Collection<RSObject> getObjectsAtLocal(int x, int y) {
        List<RSObject> objects = new ArrayList<RSObject>();
        Client client = getClient();
        if (client == null) {
            System.out.println("Null Client");
        }
        WorldController worldController = client.getWorld();
        if (worldController == null) {
            System.out.println("Null WorldController");
        }
        Ground[][][] grounds = worldController.getGroundArray();
        if (grounds == null) {
            System.out.println("Null Ground[][][]");
        }
        Ground ground = grounds[client.getPlane()][x][y];

        if (ground != null) {
            try {
                if (ground.getGroundDecoration() != null) {
                    objects.add(new RSObject(ground.getGroundDecoration(), x, y));
                }

                if (ground.getWallDecoration() != null) {
                    objects.add(new RSObject(ground.getWallDecoration(), x, y));
                }

                if (ground.getWallObject() != null) {
                    objects.add(new RSObject(ground.getWallObject(), x, y));
                }

                if (ground.getAnimableObjects() != null) {
                    for (AnimableObject object : ground.getAnimableObjects()) {
                        RSObject wrapped = new RSObject(object, x, y);
                        if (object != null && wrapped.getId() != 0) {
                            objects.add(wrapped);
                        }
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return objects;
    }

}
