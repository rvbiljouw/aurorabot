package ms.aurora.sdn;

import ms.aurora.Application;
import ms.aurora.sdn.net.IncomingPacket;
import ms.aurora.sdn.net.OutgoingPacket;
import ms.aurora.sdn.net.PacketHandler;
import ms.aurora.sdn.net.encode.MD5;
import org.apache.log4j.Logger;

import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.util.List;

import static com.google.common.collect.Lists.newArrayList;
import static org.apache.log4j.Logger.getLogger;

/**
 * @author rvbiljouw
 */
public class SDNConnection implements Runnable {
    private static final SDNConnection instance = new SDNConnection();
    private static Logger logger = getLogger(SDNConnection.class);
    private List<PacketHandler> packetHandlers = newArrayList();
    private DataInputStream dis;
    private DataOutputStream dos;
    private Socket socket;
    private Thread self;

    public SDNConnection() {

    }

    public static SDNConnection getInstance() {
        return instance;
    }

    public void start() {
        self = new Thread(this);
        self.start();
    }

    public void stop() {
        self.interrupt();
    }

    @Override
    public void run() {
        try {
            socket = new Socket("208.94.241.76", 65500);
            socket.setKeepAlive(true);
            dis = new DataInputStream(socket.getInputStream());
            dos = new DataOutputStream(socket.getOutputStream());
            while (socket.isConnected() && !self.isInterrupted()) {
                IncomingPacket packet = new IncomingPacket(dis.readInt(), dis);
                for (PacketHandler handler : packetHandlers) {
                    if (handler.getOpcode() == packet.getOpcode()) {
                        handler.handle(packet);
                        break;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(255);
        }
    }

    public void writePacket(OutgoingPacket packet) {
        try {
            byte[] buffer = packet.getPayload();
            dos.write(buffer, 0, buffer.length);
            dos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void checkUpdate() {
        try {
            String path = Application.class.getProtectionDomain().getCodeSource().getLocation().getFile();
            if (!path.endsWith(".jar")) {
                logger.info("Not running inside jar, not checking for updates!");
                return;
            }
            File file = new File(path);
            String digest = MD5.digest(file);
            dos.writeInt(1);
            dos.writeUTF(digest);
            dos.flush();

            int response = dis.readInt();
            switch (response) {
                case 0:
                    logger.info("Client is up-to-date");
                    break;

                case 1:
                    logger.info("Update available.");
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
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
