package ms.aurora.api.wrappers;

import ms.aurora.api.ClientContext;
import ms.aurora.api.Projection;
import ms.aurora.api.rt3.Item;

import java.awt.*;

/**
 * Created with IntelliJ IDEA.
 * User: tobiewarburton
 * Date: 16/03/2013
 * Time: 01:11
 * To change this template use File | Settings | File Templates.
 */
public class RSGroundItem implements Locatable {
    private ClientContext context;
    private Item item;
    private int localX;
    private int localY;
    private int z;

    public RSGroundItem(ClientContext context, Item item, int localX, int localY, int z) {
        this.context = context;
        this.item = item;
        this.localX = localX;
        this.localY = localY;
        this.z = z;
    }

    public int getId() {
        return item.getId();
    }

    public int getStackSize() {
        return item.getStackSize();
    }

    public Point getScreenLocation() {
        return Projection.worldToScreen(getRegionalLocation());
    }

    public RSTile getLocation() {
        return new RSTile(getX(), getY(), z);
    }

    public RSTile getRegionalLocation() {
        int x = getLocalX();
        int y = getLocalY();
        return new RSTile(x, y, z);
    }

    public int distance(Locatable other) {
        return (int) Point.distance(getX(), getY(), other.getX(), other.getY());
    }

    public int getX() {
        return getLocalX() + context.getClient().getBaseX();
    }

    public int getY() {
        return getLocalY() + context.getClient().getBaseY();
    }

    public int getLocalX() {
        return localX;
    }

    public int getLocalY() {
        return localY;
    }
}
