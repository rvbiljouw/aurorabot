package ms.aurora.core.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * @author rvbiljouw
 */
@Entity
public class MapTile extends AbstractModel {

    @Id
    @GeneratedValue
    private Long id;

    private int x;
    private int y;
    private byte mask;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public byte getMask() {
        return mask;
    }

    public void setMask(byte mask) {
        this.mask = mask;
    }

    public static MapTile getAt(int x, int y) {
        return finder(MapTile.class).where().eq("x", x).eq("y", y).findUnique();
    }
}
