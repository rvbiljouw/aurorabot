package ms.aurora.sdn.net.packet;

import ms.aurora.sdn.net.OutgoingPacket;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: rvbiljouw
 * Date: 13-5-13
 * Time: 0:59
 * To change this template use File | Settings | File Templates.
 */
public class ScriptCountRequest extends OutgoingPacket {
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
