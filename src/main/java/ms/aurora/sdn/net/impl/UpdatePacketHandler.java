package ms.aurora.sdn.net.impl;

import ms.aurora.sdn.net.IncomingPacket;
import ms.aurora.sdn.net.PacketHandler;

import javax.swing.*;
import java.io.IOException;

import static ms.aurora.Application.logger;

/**
 * @author rvbiljouw
 */
public class UpdatePacketHandler implements PacketHandler {
    @Override
    public int getOpcode() {
        return 2;
    }

    @Override
    public void handle(IncomingPacket incomingPacket) throws IOException {
        int responseCode = incomingPacket.getStream().readInt();
        switch (responseCode) {
            case 0:
                logger.info("Client is up-to-date");
                break;

            case 1:
                JOptionPane.showMessageDialog(null, "This release is old.\nA new release is available from http://www.aurora.ms !");
                break;
        }
    }
}
