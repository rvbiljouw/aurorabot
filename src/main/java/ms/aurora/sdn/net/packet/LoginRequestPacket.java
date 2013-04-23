package ms.aurora.sdn.net.packet;

import ms.aurora.sdn.net.OutgoingPacket;
import ms.aurora.sdn.net.encode.AES;

import java.io.IOException;

import static ms.aurora.sdn.net.encode.Base64.encode;

/**
 * Sends a login request to the server.
 * @author rvbiljouw
 */
public class LoginRequestPacket extends OutgoingPacket {
    private final String username;
    private final String password;

    public LoginRequestPacket(String username, String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    public int getOpcode() {
        return 1;
    }

    @Override
    public void prepare() throws IOException {
        getStream().writeUTF(encode(AES.encryptString(username)));
        getStream().writeUTF(encode(AES.encryptString(password)));
        getStream().flush();
        getStream().close();
    }
}
