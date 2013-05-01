package ms.aurora.sdn.net.impl;

import ms.aurora.browser.wrapper.Plaintext;
import ms.aurora.sdn.net.IncomingPacket;
import ms.aurora.sdn.net.PacketHandler;
import ms.aurora.sdn.net.api.Hooks;
import ms.aurora.transform.ClientDefinition;

import java.io.IOException;

import static ms.aurora.sdn.net.encode.AES.decryptString;
import static ms.aurora.sdn.net.encode.Base64.decode;

/**
 * @author rvbiljouw
 */
public class HookPacketHandler implements PacketHandler {

    @Override
    public int getOpcode() {
        return 4;
    }

    @Override
    public void handle(IncomingPacket incomingPacket) throws IOException {
        Plaintext plainText = new Plaintext(decryptString(decode(incomingPacket.getStream().readUTF())));
        Hooks.setHooks(new ClientDefinition(plainText));
        Hooks.getHooks().load();
    }

}
