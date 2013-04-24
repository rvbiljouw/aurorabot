package ms.aurora.sdn.net.impl;

import javafx.application.Platform;
import ms.aurora.Application;
import ms.aurora.sdn.net.IncomingPacket;
import ms.aurora.sdn.net.PacketHandler;

import java.io.IOException;

import static ms.aurora.Application.LOGIN_WINDOW;
import static ms.aurora.sdn.net.encode.AES.decrypt;
import static ms.aurora.sdn.net.encode.Base64.decode;

/**
 * Handles the authentication
 *
 * @author rvbiljouw
 */
public class LoginPacketHandler implements PacketHandler {

    @Override
    public int getOpcode() {
        return 1;
    }

    @Override
    public void handle(IncomingPacket incomingPacket) {
        try {
            String returnMessage = incomingPacket.getStream().readUTF();
            String decryptedMessage = new String(decrypt(decode(returnMessage)));
            if (decryptedMessage.equals("ok")) {
                Platform.runLater(new Runnable() {
                    @Override
                    public void run() {
                        LOGIN_WINDOW.close();
                        Application.showStage();
                    }
                });

            } else {
                LOGIN_WINDOW.setMessage(decryptedMessage);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to handle authentication packet.");
        }
    }

}
