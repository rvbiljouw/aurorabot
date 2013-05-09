package ms.aurora.sdn.net.impl;

import ms.aurora.core.plugin.PluginLoader;
import ms.aurora.core.script.ScriptLoader;
import ms.aurora.sdn.net.IncomingPacket;
import ms.aurora.sdn.net.PacketHandler;
import ms.aurora.sdn.net.api.Repository;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarInputStream;

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
        DataInputStream in = incomingPacket.getStream();
        int count = in.readInt();
        List<JarInputStream> streams = new ArrayList<JarInputStream>(count);
        for (int i = 0; i < count; i++) {
            int len = in.readInt();
            byte[] bytes = new byte[len];
            int res = in.read(bytes); // todo maybe add a crc
            streams.add(new JarInputStream(new ByteArrayInputStream(bytes)));
        }
        PluginLoader.remoteStreams = streams;
        synchronized (Repository.plugin_lock) {
            Repository.plugin_lock.notifyAll();
        }
    }
}