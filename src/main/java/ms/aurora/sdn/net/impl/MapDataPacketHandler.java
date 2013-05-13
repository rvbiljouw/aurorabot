package ms.aurora.sdn.net.impl;

import ms.aurora.api.pathfinding.impl.RSMap;
import ms.aurora.sdn.net.IncomingPacket;
import ms.aurora.sdn.net.PacketHandler;
import org.apache.log4j.Logger;

import java.io.DataInputStream;
import java.io.IOException;

/**
 * Handles the mapdata parser.
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
            int rx = in.readInt();
            int ry = in.readInt();
            int len = in.readInt();
            for (int i = 0; i < len; i++) {
                int sublen = in.readInt();
                for (int j = 0; j < sublen; j++) {
                    int mask = in.readInt();
                    if ((mask & (RSMap.BLOCKED | RSMap.INVALID)) != 0) {
                        RSMap.CLIPPING_MASKS[rx + i][ry + j] = -128;
                    } else {
                        RSMap.CLIPPING_MASKS[rx + i][ry + j] = (byte) mask;
                    }
                }
            }
        }
    }

}
