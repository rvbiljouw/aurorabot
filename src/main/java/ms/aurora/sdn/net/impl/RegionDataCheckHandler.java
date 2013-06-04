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
        int baseX = in.readInt();
        int baseY = in.readInt();
        int plane = in.readInt();
        RegionDataPacket packet = RegionDataPacket.get(baseX, baseY, plane);
        if (packet != null) {
            if (response == 1) {
                SDNConnection.instance.writePacket(new RegionData(packet));

            }
            packet.delete();
        }
    }
}