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
public class Objects {

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
            if (ground.getObject1() != null) {
                objects.add(new RSObject(context.get(), ground.getObject1(), x, y));
            } else if (ground.getObject2() != null) {
                objects.add(new RSObject(context.get(), ground.getObject2(), x, y));
            } else if (ground.getObject4() != null) {
                objects.add(new RSObject(context.get(), ground.getObject4(), x, y));
            } else if (ground.getAnimableObjects() != null) {
                for (AnimableObject object : ground.getAnimableObjects()) {
                    if (object != null && (object.getId() >> 29 & 0x3) == 2) {
                        objects.add(new RSObject(context.get(), object, x, y));
                    }
                }
            }
        }
        return objects;
    }

}
