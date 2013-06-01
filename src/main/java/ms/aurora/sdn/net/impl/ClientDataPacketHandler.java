package ms.aurora.sdn.net.impl;

import ms.aurora.browser.wrapper.Plaintext;
import ms.aurora.sdn.net.IncomingPacket;
import ms.aurora.sdn.net.PacketHandler;
import ms.aurora.sdn.net.api.ClientData;
import ms.aurora.sdn.net.encode.AES;

import java.io.IOException;

import static ms.aurora.sdn.net.encode.AES.decryptString;
import static ms.aurora.sdn.net.encode.Base64.decode;

/**
 * @author rvbiljouw
 */
public class ClientDataPacketHandler implements PacketHandler {

    @Override
    public int getOpcode() {
        return 4;
    }

    @Override
    public void handle(IncomingPacket incomingPacket) throws IOException {
        int length = incomingPacket.getStream().readInt();
        byte[] data = new byte[length];
        incomingPacket.getStream().readFully(data);
        Plaintext plainText = new Plaintext(AES.decryptString(data));
        ClientData.setData(plainText);
    }

}
