package ms.aurora.api.methods;

import com.google.common.collect.Collections2;
import ms.aurora.api.ClientContext;
import ms.aurora.api.util.Predicate;
import ms.aurora.api.wrappers.RSObject;
import ms.aurora.rt3.AnimableObject;
import ms.aurora.rt3.Client;
import ms.aurora.rt3.Ground;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

/**
 * @author rvbiljouw
 */
public final class Objects {
    private final ClientContext ctx;

    public Objects(ClientContext ctx) {
        this.ctx = ctx;
    }

    /**
     * a method which gets the closest {@link RSObject} in the current region which satisfy the {@link Predicate}
     *
     * @param predicate the {@link Predicate} which needs to be satisfied
     * @return the closest {@link RSObject} which satisfies the given {@link Predicate} else null
     * @see RSObject#distance(ms.aurora.api.wrappers.Locatable)
     * @see Predicate
     */
    public RSObject get(final Predicate<RSObject> predicate) {
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
    public RSObject[] getAll(final Predicate<RSObject> predicate) {
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
    public RSObject[] getAll() {
        return _getAll().toArray(new RSObject[0]);
    }

    private RSObject getClosest(RSObject[] objects) {
        RSObject closest = null;
        int closestDistance = 9999;
        for (RSObject object : objects) {
            int distance = object.distance(ctx.players.getLocal());
            if (distance < closestDistance) {
                closestDistance = distance;
                closest = object;
            }
        }
        return closest;
    }

    private List<RSObject> _getAll() {
        List<RSObject> objects = newArrayList();
        for (int x = 0; x < 104; x++) {
            for (int y = 0; y < 104; y++) {
                objects.addAll(getObjectsAt(x, y));
            }
        }
        return objects;
    }

    private List<RSObject> getObjectsAt(int x, int y) {
        Client client = ctx.getClient();
        Ground ground = client.getWorld().getGroundArray()[client.getPlane()][x][y];

        List<RSObject> objects = newArrayList();
        if (ground != null) {
            try {
                if (ground.getGroundDecoration() != null) {
                    objects.add(new RSObject(ctx, ground.getGroundDecoration(), x, y));
                }

                if (ground.getWallDecoration() != null) {
                    objects.add(new RSObject(ctx, ground.getWallDecoration(), x, y));
                }

                if (ground.getWallObject() != null) {
                    objects.add(new RSObject(ctx, ground.getWallObject(), x, y));
                }

                if (ground.getAnimableObjects() != null) {
                    for(AnimableObject object : ground.getAnimableObjects()) {
                        RSObject wrapped = new RSObject(ctx, object, x ,y);
                        if(object != null && wrapped.getId() != 0) {
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
