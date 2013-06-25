package ms.aurora.api.methods;

import ms.aurora.api.methods.query.impl.ObjectQuery;
import ms.aurora.api.util.ArrayUtils;
import ms.aurora.api.util.Predicate;
import ms.aurora.api.wrappers.GameObject;
import ms.aurora.rt3.IAnimableObject;
import ms.aurora.rt3.IClient;
import ms.aurora.rt3.IGround;
import ms.aurora.rt3.IWorldController;

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
     * a method which gets the closest {@link ms.aurora.api.wrappers.GameObject} in the current region which satisfy the {@link Predicate}
     *
     * @param predicates the list of {@link Predicate} which needs to be satisfied
     * @return the closest {@link ms.aurora.api.wrappers.GameObject} which satisfies the given {@link Predicate} else null
     * @see ms.aurora.api.wrappers.GameObject#distance(ms.aurora.api.wrappers.Locatable)
     * @see Predicate
     */
    public static GameObject get(final Predicate<GameObject>... predicates) {
        return getClosest(ArrayUtils.filter(getAll(), predicates).toArray(new GameObject[0]));
    }

    /**
     * a method which gets all the {@link ms.aurora.api.wrappers.GameObject} in the current region which satisfy the given {@link Predicate}
     *
     * @param predicates the {@link Predicate} which needs to be satisfied
     * @return an array containing all of the {@link ms.aurora.api.wrappers.GameObject} which satisfy the predicate
     * @see Predicate
     */
    public static GameObject[] getAll(final Predicate<GameObject>... predicates) {
        return ArrayUtils.filter(getAll(), predicates).toArray(new GameObject[0]);
    }

    /**
     * a method which gets all the {@link ms.aurora.api.wrappers.GameObject} in the current region
     *
     * @return an array of {@link ms.aurora.api.wrappers.GameObject} in the current region
     */
    public static GameObject[] getAll() {
        List<GameObject> objects = new ArrayList<GameObject>();
        for (int x = 0; x < 104; x++) {
            for (int y = 0; y < 104; y++) {
                objects.addAll(getObjectsAtLocal(x, y));
            }
        }
        return objects.toArray(new GameObject[objects.size()]);
    }

    /**
     * Gets the closest object out of an array of objects
     *
     * @param objects Array of objects
     * @return Closest object
     */
    private static GameObject getClosest(GameObject[] objects) {
        GameObject closest = null;
        int closestDistance = 9999;
        for (GameObject object : objects) {
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
    public static Collection<GameObject> getObjectsAt(int x, int y) {
        return getObjectsAtLocal(x - getClient().getBaseX(), y - getClient().getBaseY());
    }

    /**
     * Gets all the objects at specified x/y
     *
     * @param x local X coordinate
     * @param y local Y coordinate
     * @return collection of objects at the specified tile
     */
    public static Collection<GameObject> getObjectsAtLocal(int x, int y) {
        List<GameObject> objects = new ArrayList<GameObject>();
        IClient client = getClient();
        IWorldController worldController = client.getWorld();
        IGround[][][] grounds = worldController.getGroundArray();
        IGround ground = grounds[client.getPlane()][x][y];

        if (ground != null) {
            try {
                if (ground.getGroundDecoration() != null) {
                    objects.add(new GameObject(ground.getGroundDecoration(), x, y));
                }

                if (ground.getWallDecoration() != null) {
                    objects.add(new GameObject(ground.getWallDecoration(), x, y));
                }

                if (ground.getWallObject() != null) {
                    objects.add(new GameObject(ground.getWallObject(), x, y));
                }

                if (ground.getAnimableObjects() != null) {
                    for (IAnimableObject object : ground.getAnimableObjects()) {
                        GameObject wrapped = new GameObject(object, x, y);
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

    public static ObjectQuery find() {
        return new ObjectQuery();
    }
}
