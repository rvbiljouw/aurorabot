package ms.aurora.sdn.net.impl;

import ms.aurora.sdn.net.IncomingPacket;
import ms.aurora.sdn.net.PacketHandler;
import ms.aurora.sdn.net.api.Repository;
import ms.aurora.sdn.net.api.repository.RemotePlugin;
import ms.aurora.sdn.net.api.repository.RemoteScript;

import java.io.DataInputStream;
import java.io.IOException;

/**
 * @author tobiewarburton
 */
public class PluginPacketHandler implements PacketHandler {

    @Override
    public int getOpcode() {
        return 6;
    }

    @Override
    public void handle(IncomingPacket incomingPacket) throws IOException {
        Repository.REMOTE_PLUGIN_LIST.clear();
        DataInputStream in = incomingPacket.getStream();
        int count = in.readInt();
        for (int i = 0; i < count; i++) {
            RemotePlugin ent = new RemotePlugin(in.readLong(), in.readUTF(), in.readUTF());
            Repository.REMOTE_PLUGIN_LIST.add(ent);
        }
        synchronized (Repository.plugin_lock) {
            Repository.plugin_lock.notifyAll();
        }
    }
}