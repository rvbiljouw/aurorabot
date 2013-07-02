package ms.aurora.sdn.net.impl;

import flexjson.JSONDeserializer;
import ms.aurora.sdn.net.IncomingPacket;
import ms.aurora.sdn.net.PacketHandler;

import java.io.DataInputStream;
import java.io.IOException;

/**
 * @author iJava
 */
public class StatsDataPacketHandler implements PacketHandler {
    @Override
    public int getOpcode() {
        return 9;
    }

    @Override
    public void handle(IncomingPacket incomingPacket) throws IOException {

    }
}
