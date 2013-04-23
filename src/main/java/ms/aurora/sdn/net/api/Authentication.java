package ms.aurora.sdn.net.api;

import ms.aurora.sdn.SDNConnection;
import ms.aurora.sdn.net.OutgoingPacket;
import ms.aurora.sdn.net.packet.LoginRequestPacket;

/**
 * @author rvbiljouw
 */
public class Authentication {

    public static void login(String username, String password) {
        OutgoingPacket packet = new LoginRequestPacket(username, password);
        SDNConnection.getInstance().writePacket(packet);
    }

}
