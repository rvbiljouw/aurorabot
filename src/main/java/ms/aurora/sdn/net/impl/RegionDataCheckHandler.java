package ms.aurora.sdn.net.impl;

import ms.aurora.sdn.SDNConnection;
import ms.aurora.sdn.model.RegionDataPacket;
import ms.aurora.sdn.net.IncomingPacket;
import ms.aurora.sdn.net.PacketHandler;
import ms.aurora.sdn.net.packet.RegionData;
import ms.aurora.sdn.net.packet.RegionDataCheck;

import java.io.DataInputStream;
import java.io.IOException;

/**
 * @author tobiewarburton
 */
public class RegionDataCheckHandler implements PacketHandler {

    @Override
    public int getOpcode() {
        return 8;
    }

    @Override
    public void handle(IncomingPacket incomingPacket) throws IOException {
        DataInputStream in = incomingPacket.getStream();
        byte response = in.readByte();
        if (response == 1) {
            int baseX = in.readInt();
            int baseY = in.readInt();
            int plane = in.readInt();
            for (RegionDataPacket check : RegionDataCheck.cache) {
                if (check.getBaseX() == baseX
                        && check.getBaseY() == baseY
                        && check.getPlane() == plane) {
                    SDNConnection.getInstance().writePacket(new RegionData(check));
                }
            }
        }
    }
}