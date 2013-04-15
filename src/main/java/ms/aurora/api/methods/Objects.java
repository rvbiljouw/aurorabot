package ms.aurora.api.methods;

import com.google.common.collect.Collections2;
import ms.aurora.api.Context;
import ms.aurora.api.util.Predicate;
import ms.aurora.api.wrappers.RSObject;
import ms.aurora.rt3.AnimableObject;
import ms.aurora.rt3.Client;
import ms.aurora.rt3.Ground;

import java.util.Collection;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

/**
 * Object related methods
 *
 * @author Rick
 */
public final class Objects {

    /**
     * a method which gets the closest {@link RSObject} in the current region which satisfy the {@link Predicate}
     *
     * @param predicate the {@link Predicate} which needs to be satisfied
     * @return the closest {@link RSObject} which satisfies the given {@link Predicate} else null
     * @see RSObject#distance(ms.aurora.api.wrappers.Locatable)
     * @see Predicate
     */
    public static RSObject get(final Predicate<RSObject>... predicates) {
        return getClosest(Collections2.filter(_getAll(),
                new com.google.common.base.Predicate<RSObject>() {
                    @Override
                    public boolean apply(RSObject rsObject) {
                        for (Predicate p : predicates) {
                            if (!p.apply(rsObject)) return false;
                        }
                        return true;
                    }
                }
        ).toArray(new RSObject[0]));
    }

    /**
     * a method which gets all the {@link RSObject} in the current region which satisfy the given {@link Predicate}
     *
     * @param predicates the {@link Predicate} which needs to be satisfied
     * @return an array containing all of the {@link RSObject} which satisfy the predicate
     * @see Predicate
     */
    public static RSObject[] getAll(final Predicate<RSObject>... predicates) {
        return Collections2.filter(_getAll(),
                new com.google.common.base.Predicate<RSObject>() {
                    @Override
                    public boolean apply(RSObject rsObject) {
                        for (Predicate p : predicates) {
                            if (!p.apply(rsObject)) return false;
                        }
                        return true;
                    }
                }
        ).toArray(new RSObject[0]);
    }

    /**
     * a method which gets all the {@link RSObject} in the current region
     *
     * @return an array of {@link RSObject} in the current region
     */
    public static RSObject[] getAll() {
        return _getAll().toArray(new RSObject[0]);
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
     * Gets a collection of all objects in the currently loaded region
     *
     * @return collection of objects
     */
    private static Collection<RSObject> _getAll() {
        List<RSObject> objects = newArrayList();
        for (int x = 0; x < 104; x++) {
            for (int y = 0; y < 104; y++) {
                objects.addAll(getObjectsAt(x, y));
            }
        }
        return objects;
    }

    /**
     * Gets all the objects at specified x/y
     *
     * @param x local X coordinate
     * @param y local Y coordinate
     * @return collection of objects at the specified tile
     */
    public static Collection<RSObject> getObjectsAt(int x, int y) {
        Client client = Context.getClient();
        Ground ground = client.getWorld().getGroundArray()[client.getPlane()][x][y];

        List<RSObject> objects = newArrayList();
        if (ground != null) {
            try {
                if (ground.getGroundDecoration() != null) {
                    objects.add(new RSObject(Context.get(), ground.getGroundDecoration(), x, y));
                }

                if (ground.getWallDecoration() != null) {
                    objects.add(new RSObject(Context.get(), ground.getWallDecoration(), x, y));
                }

                if (ground.getWallObject() != null) {
                    objects.add(new RSObject(Context.get(), ground.getWallObject(), x, y));
                }

                if (ground.getAnimableObjects() != null) {
                    for (AnimableObject object : ground.getAnimableObjects()) {
                        RSObject wrapped = new RSObject(Context.get(), object, x, y);
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
