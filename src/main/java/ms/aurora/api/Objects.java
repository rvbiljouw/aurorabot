package ms.aurora.api;

import com.google.common.collect.Collections2;
import ms.aurora.api.rt3.AnimableObject;
import ms.aurora.api.rt3.Client;
import ms.aurora.api.rt3.Ground;
import ms.aurora.api.util.Predicate;
import ms.aurora.api.wrappers.RSObject;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static ms.aurora.api.ClientContext.context;

/**
 * @author rvbiljouw
 */
public final class Objects {

    private Objects() { }

    /**
     * a method which gets the closest {@link RSObject} in the current region which satisfy the {@link Predicate}
     *
     * @param predicate the {@link Predicate} which needs to be satisfied
     * @return the closest {@link RSObject} which satisfies the given {@link Predicate} else null
     * @see RSObject#distance(ms.aurora.api.wrappers.Locatable)
     * @see Predicate
     */
    public static RSObject get(final Predicate<RSObject> predicate) {
        return getClosest(Collections2.filter(_getAll(),
                new com.google.common.base.Predicate<RSObject>() {
                    @Override
                    public boolean apply(RSObject rsObject) {
                        return predicate.apply(rsObject);
                    }
                }
        ).toArray(new RSObject[0]));
    }

    /**
     * a method which gets all the {@link RSObject} in the current region which satisfy the given {@link Predicate}
     *
     * @param predicate the {@link Predicate} which needs to be satisfied
     * @return an array containing all of the {@link RSObject} which satisfy the predicate
     * @see Predicate
     */
    public static RSObject[] getAll(final Predicate<RSObject> predicate) {
        return Collections2.filter(_getAll(),
                new com.google.common.base.Predicate<RSObject>() {
                    @Override
                    public boolean apply(RSObject rsObject) {
                        return predicate.apply(rsObject);
                    }
                }
        ).toArray(new RSObject[]{});
    }

    /**
     * a method which gets all the {@link RSObject} in the current region
     *
     * @return an array of {@link RSObject} in the current region
     */
    public static RSObject[] getAll() {
        return _getAll().toArray(new RSObject[0]);
    }

    private static RSObject getClosest(RSObject[] objects) {
        RSObject closest = null;
        int closestDistance = 9999;
        for (RSObject object : objects) {
            int distance = object.distance(context.get().getMyPlayer());
            if (distance < closestDistance) {
                closestDistance = distance;
                closest = object;
            }
        }
        return closest;
    }

    private static List<RSObject> _getAll() {
        List<RSObject> objects = newArrayList();
        for (int x = 0; x < 104; x++) {
            for (int y = 0; y < 104; y++) {
                objects.addAll(getObjectsAt(x, y));
            }
        }
        return objects;
    }

    private static List<RSObject> getObjectsAt(int x, int y) {
        Client client = context.get().getClient();
        Ground ground = client.getWorld().getGroundArray()[client.getPlane()][x][y];

        List<RSObject> objects = newArrayList();
        if (ground != null) {
            if (ground.getGroundDecoration() != null) {
                objects.add(new RSObject(context.get(), ground.getGroundDecoration()));
            } else if (ground.getWallDecoration() != null) {
                objects.add(new RSObject(context.get(), ground.getWallDecoration()));
            } else if (ground.getWallObject() != null) {
                objects.add(new RSObject(context.get(), ground.getWallObject()));
            } else if (ground.getAnimableObjects() != null) {
                for (AnimableObject object : ground.getAnimableObjects()) {
                    RSObject fuckyou = new RSObject(context.get(), object);
                    if (object != null && fuckyou.getId() != 0) {
                        objects.add(fuckyou);
                    }
                }
            }
        }
        return objects;
    }

}
