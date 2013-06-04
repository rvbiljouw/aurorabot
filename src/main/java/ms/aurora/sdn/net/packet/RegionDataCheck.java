package ms.aurora.sdn.net.packet;

import ms.aurora.sdn.model.RegionDataPacket;
import ms.aurora.sdn.net.OutgoingPacket;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author tobiewarburton
 */
public class RegionDataCheck extends OutgoingPacket {
    public static List<RegionDataPacket> cache = new ArrayList<RegionDataPacket>();
    private int baseX;
    private int baseY;
    private int plane;
    private int[][] masks;


    public RegionDataCheck(int baseX, int baseY, int plane, int[][] masks) {
        this.baseX = baseX;
        this.baseY = baseY;
        this.plane = plane;
        this.masks = masks;
        cache.add(new RegionDataPacket(baseX, baseY, plane, masks));
    }

    @Override
    public int getOpcode() {
        return 8;
    }

    @Override
    public void prepare() throws IOException {
        getStream().writeInt(baseX);
        getStream().writeInt(baseY);
        getStream().writeInt(plane);
        getStream().flush();
        getStream().close();
    }

    public int getBaseX() {
        return baseX;
    }

    public int getBaseY() {
        return baseY;
    }

    public int getPlane() {
        return plane;
    }

    public int[][] getMasks() {
        return masks;
    }
}
