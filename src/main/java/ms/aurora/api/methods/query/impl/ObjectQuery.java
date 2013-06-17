package ms.aurora.api.methods.query.impl;

import ms.aurora.api.wrappers.GameObject;
import ms.aurora.rt3.IAnimableObject;
import ms.aurora.rt3.IClient;
import ms.aurora.rt3.IGround;
import ms.aurora.rt3.IWorldController;

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
public final class ObjectQuery extends LocatableQuery<GameObject, ObjectQuery> {

    @Override
    public GameObject[] result() {
        List<GameObject> rsObjects = getAll();
        rsObjects = filterResults(rsObjects);
        Collections.sort(rsObjects, DISTANCE_COMPARATOR);
        return rsObjects.toArray(new GameObject[rsObjects.size()]);
    }

    public ObjectQuery id(final int... ids) {
        this.addConditional(new Conditional() {
            @Override
            protected boolean accept(GameObject type) {
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

    private List<GameObject> getAll() {
        List<GameObject> objects = new ArrayList<GameObject>();
        for (int x = 0; x < 104; x++) {
            for (int y = 0; y < 104; y++) {
                objects.addAll(getObjectsAtLocal(x, y));
            }
        }
        return objects;
    }

    private List<GameObject> getObjectsAtLocal(int x, int y) {
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

}
