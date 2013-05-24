package ms.aurora.api.methods.query.impl;

import ms.aurora.api.methods.query.Sort;
import ms.aurora.api.wrappers.RSObject;
import ms.aurora.rt3.AnimableObject;
import ms.aurora.rt3.Client;
import ms.aurora.rt3.Ground;
import ms.aurora.rt3.WorldController;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static ms.aurora.api.Context.getClient;

/**
 * Date: 24/05/13
 * Time: 12:46
 *
 * @author A_C/Cov
 */
public class ObjectQuery extends LocatableQuery<RSObject, ObjectQuery> {

    @Override
    public RSObject[] result() {
        List<RSObject> rsObjects = getAll();
        if (this.comparator == null) {
            switch (sortType) {
                case DISTANCE:
                    comparator = Sort.DISTANCE;
                    break;
                default:
                    comparator = Sort.DEFAULT;
            }
        }
        Collections.sort(filterResults(rsObjects), comparator);
        return rsObjects.toArray(new RSObject[rsObjects.size()]);
    }

    public ObjectQuery id(final int... ids) {
        this.addConditional(new Conditional() {
            @Override
            protected boolean accept(RSObject type) {
                for (int id: ids) {
                    if (type.getId() == id) {
                        return true;
                    }
                }
                return false;
            }
        });
        return this;
    }

    private List<RSObject> getAll() {
        List<RSObject> objects = new ArrayList<RSObject>();
        for (int x = 0; x < 104; x++) {
            for (int y = 0; y < 104; y++) {
                objects.addAll(getObjectsAtLocal(x, y));
            }
        }
        return objects;
    }

    private List<RSObject> getObjectsAtLocal(int x, int y) {
        List<RSObject> objects = new ArrayList<RSObject>();
        Client client = getClient();
        WorldController worldController = client.getWorld();
        Ground[][][] grounds = worldController.getGroundArray();
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
