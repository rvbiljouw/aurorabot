package ms.aurora.sdn.net.impl;

import ms.aurora.api.pathfinding.impl.RSMap;
import ms.aurora.sdn.net.IncomingPacket;
import ms.aurora.sdn.net.PacketHandler;

import java.io.DataInputStream;
import java.io.IOException;

/**
 * Handles the mapdata parser.
 *
 * @author rvbiljouw
 */
public class MapDataPacketHandler implements PacketHandler {

    @Override
    public int getOpcode() {
        return 3;
    }

    @Override
    public void handle(IncomingPacket incomingPacket) throws IOException {
        DataInputStream in = incomingPacket.getStream();
        while (in.available() > 0 && in.readByte() != -1) {
            int baseX = in.readInt();
            int baseY = in.readInt();
            int width = in.readInt();
            for (int localX = 0; localX < width; localX++) {
                int height = in.readInt();
                for (int localY = 0; localY < height; localY++) {
                    int mask = in.readInt();
                    if ((mask & (RSMap.BLOCKED | RSMap.INVALID)) != 0) {
                        RSMap.CLIPPING_MASKS[0][baseX + localX][baseY + localY] = -128;
                    } else {
                        RSMap.CLIPPING_MASKS[0][baseX + localX][baseY + localY] = (byte) mask;
                    }
                }
            }
        }
    }

}