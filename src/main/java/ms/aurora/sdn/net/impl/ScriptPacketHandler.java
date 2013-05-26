package ms.aurora.sdn.net.impl;

import ms.aurora.sdn.net.IncomingPacket;
import ms.aurora.sdn.net.PacketHandler;
import ms.aurora.sdn.net.api.Repository;
import ms.aurora.sdn.net.api.repository.RemoteScript;
import org.apache.log4j.Logger;

import java.io.DataInputStream;
import java.io.IOException;

/**
 * @author tobiewarburton
 */
public class ScriptPacketHandler implements PacketHandler {
    private static final Logger logger = Logger.getLogger(ScriptPacketHandler.class);

    @Override
    public int getOpcode() {
        return 5;
    }

    @Override
    public void handle(IncomingPacket incomingPacket) throws IOException {
        Repository.REMOTE_SCRIPT_LIST.clear();
        DataInputStream in = incomingPacket.getStream();
        int count = in.readInt();
        for (int i = 0; i < count; i++) {
            RemoteScript ent = new RemoteScript(in.readLong(), in.readUTF(), in.readUTF());
            Repository.REMOTE_SCRIPT_LIST.add(ent);
        }
        synchronized (Repository.script_lock) {
            Repository.script_lock.notifyAll();
        }
    }
}