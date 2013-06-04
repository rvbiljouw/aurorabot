package ms.aurora.sdn.model;

/**
 * @author tobiewarburton
 */
public class RegionDataPacket {
    private int baseX;
    private int baseY;
    private int plane;
    private int[][] masks;

    public RegionDataPacket(int baseX, int baseY, int plane, int[][] masks) {
        this.baseX = baseX;
        this.baseY = baseY;
        this.plane = plane;
        this.masks = masks;
    }

    public RegionDataPacket() {

    }

    public int getBaseX() {
        return baseX;
    }

    public void setBaseX(int baseX) {
        this.baseX = baseX;
    }

    public int getBaseY() {
        return baseY;
    }

    public void setBaseY(int baseY) {
        this.baseY = baseY;
    }

    public int getPlane() {
        return plane;
    }

    public void setPlane(int plane) {
        this.plane = plane;
    }

    public int[][] getMasks() {
        return masks;
    }

    public void setMasks(int[][] masks) {
        this.masks = masks;
    }
}
