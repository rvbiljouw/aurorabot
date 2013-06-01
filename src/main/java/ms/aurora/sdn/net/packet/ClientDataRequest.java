package ms.aurora.sdn.net.packet;

import ms.aurora.sdn.net.OutgoingPacket;

import java.io.IOException;

/**
 * @author rvbiljouw
 */
public class ClientDataRequest extends OutgoingPacket {
    @Override
    public int getOpcode() {
        return 4;
    }

    @Override
    public void prepare() throws IOException {
        getStream().flush();
        getStream().close();
    }
}
