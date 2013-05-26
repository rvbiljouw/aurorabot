package ms.aurora.sdn.net.packet;

import ms.aurora.sdn.net.OutgoingPacket;

import java.io.IOException;

/**
 * @author tobiewarburton
 */
public class ScriptRequest extends OutgoingPacket {

    @Override
    public int getOpcode() {
        return 5;
    }

    @Override
    public void prepare() throws IOException {
        getStream().flush();
        getStream().close();
    }
}
