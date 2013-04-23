package ms.aurora.sdn.net.impl;

import ms.aurora.sdn.net.IncomingPacket;
import ms.aurora.sdn.net.PacketHandler;
import ms.aurora.sdn.net.api.Versioning;

import javax.swing.*;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
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
                logger.info("Update available.");
                handleUpdate(Versioning.FILE, incomingPacket.getStream());
                break;
        }
    }

    private void handleUpdate(File file, DataInputStream dis) throws IOException {
        FileOutputStream fos = new FileOutputStream(file);
        long fileSize = dis.readLong();
        long totalRead = 0;
        int read = 0;
        byte[] buffer = new byte[256];
        while (totalRead != fileSize) {
            read = dis.read(buffer);
            totalRead += read;
            fos.write(buffer, 0, read);
        }
        fos.flush();
        fos.close();
        JOptionPane.showMessageDialog(null, "The client was updated, please re-start!");
        System.exit(0);
    }
}
