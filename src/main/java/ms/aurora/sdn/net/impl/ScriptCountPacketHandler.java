package ms.aurora.sdn.net.impl;

import ms.aurora.sdn.net.IncomingPacket;
import ms.aurora.sdn.net.PacketHandler;
import ms.aurora.sdn.net.api.Repository;

import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: rvbiljouw
 * Date: 13-5-13
 * Time: 0:59
 * To change this template use File | Settings | File Templates.
 */
public class ScriptCountPacketHandler implements PacketHandler {
    @Override
    public int getOpcode() {
        return 5;
    }

    @Override
    public void handle(IncomingPacket incomingPacket) throws IOException {
        Repository.REMOTE_SCRIPT_COUNT = incomingPacket.getStream().readInt();
        synchronized (Repository.script_count_lock) {
            Repository.script_count_lock.notifyAll();
        }
    }
}
