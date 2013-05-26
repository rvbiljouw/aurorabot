package ms.aurora.sdn.net.impl;

import ms.aurora.sdn.net.IncomingPacket;
import ms.aurora.sdn.net.PacketHandler;
import ms.aurora.sdn.net.api.Repository;
import ms.aurora.sdn.net.api.repository.Requestable;

import java.io.DataInputStream;
import java.io.IOException;

/**
 * Created with IntelliJ IDEA.
 * User: rvbiljouw
 * Date: 13-5-13
 * Time: 0:59
 * To change this template use File | Settings | File Templates.
 */
public class FilePacketHandler implements PacketHandler {
    @Override
    public int getOpcode() {
        return 7;
    }

    @Override
    public void handle(IncomingPacket incomingPacket) throws IOException {
        DataInputStream in = incomingPacket.getStream();
        switch (in.readByte()) {
            case 0:
                int len = in.readInt();
                byte[] bytes = new byte[len];
                int result = in.read(bytes);
                Repository.store = bytes;
                break;
            case 1:
                // error!!!!!!!!!!!!!!!!!!!!!!!!!
                break;
        }
        synchronized (Requestable.request_lock) {
            Requestable.request_lock.notifyAll();
        }
    }
}
