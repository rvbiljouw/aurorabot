package ms.aurora.sdn.net.packet;

import ms.aurora.sdn.net.OutgoingPacket;

import java.io.IOException;

/**
 * Packet that requests mapdata from the server
 * @author rvbiljouw
 */
public class MapDataRequest extends OutgoingPacket {
    @Override
    public int getOpcode() {
        return 3;
    }

    @Override
    public void prepare() throws IOException {
        // Packet consists only of opcode.
        getStream().flush();
        getStream().close();
    }
}
