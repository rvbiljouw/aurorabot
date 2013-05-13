package ms.aurora.sdn.net.impl;

import ms.aurora.core.script.ScriptLoader;
import ms.aurora.sdn.net.IncomingPacket;
import ms.aurora.sdn.net.PacketHandler;
import ms.aurora.sdn.net.api.Repository;
import ms.aurora.util.JarInputStreamClassLoader;
import org.apache.log4j.Logger;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarInputStream;

/**
 * @author tobiewarburton
 */
public class ScriptPacketHandler implements PacketHandler {
    private static final Logger logger = Logger.getLogger(ScriptPacketHandler.class);

    @Override
    public int getOpcode() {
        return 7;
    }

    @Override
    public void handle(IncomingPacket incomingPacket) throws IOException {
        DataInputStream in = incomingPacket.getStream();
        int len = in.readInt();
        byte[] bytes = new byte[len];
        int res = in.read(bytes); // todo maybe add a crc
        ScriptLoader.remoteStreams.add(new JarInputStream(new ByteArrayInputStream(bytes)));
        synchronized (Repository.script_lock) {
            Repository.script_lock.notifyAll();
        }
    }
}