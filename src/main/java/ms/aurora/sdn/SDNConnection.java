package ms.aurora.sdn;

import ms.aurora.sdn.net.IncomingPacket;
import ms.aurora.sdn.net.OutgoingPacket;
import ms.aurora.sdn.net.PacketHandler;
import ms.aurora.sdn.net.impl.*;
import org.apache.log4j.Logger;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import static org.apache.log4j.Logger.getLogger;

/**
 * @author rvbiljouw
 */
public class SDNConnection implements Runnable {
    public static final SDNConnection instance = new SDNConnection();
    private static Logger logger = getLogger(SDNConnection.class);
    private List<PacketHandler> packetHandlers = new ArrayList<PacketHandler>();
    private DataInputStream dis;
    private DataOutputStream dos;
    private Socket socket;
    private Thread self;

    public SDNConnection() {

    }

    public static synchronized SDNConnection getInstance() {
        return instance;
    }

    public boolean start() {
        try {
            socket = new Socket("sdn.aurora.ms", 443);
            socket.setSoTimeout(5000);
            socket.setKeepAlive(true);
            dis = new DataInputStream(socket.getInputStream());
            dos = new DataOutputStream(socket.getOutputStream());
            self = new Thread(this);
            self.start();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            logger.info("Connection to SDN failed..");
            logger.info("Retrying in 1s...");
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e1) {
                e1.printStackTrace();
            }
            return false;
        }
    }

    public void stop() {
        self.interrupt();
    }

    @Override
    public void run() {
        try {
            packetHandlers.clear();
            packetHandlers.add(new LoginPacketHandler());
            packetHandlers.add(new UpdatePacketHandler());
            packetHandlers.add(new MapDataPacketHandler());
            packetHandlers.add(new ClientDataPacketHandler());
            packetHandlers.add(new ScriptPacketHandler());
            packetHandlers.add(new PluginPacketHandler());
            packetHandlers.add(new FilePacketHandler());
            packetHandlers.add(new RegionDataCheckHandler());

            while (socket.isConnected() && !self.isInterrupted()) {
                if (dis.available() > 0) {
                    IncomingPacket packet = new IncomingPacket(dis.readInt(), dis);
                    for (PacketHandler handler : packetHandlers) {
                        if (handler.getOpcode() == packet.getOpcode()) {
                            handler.handle(packet);
                            break;
                        }
                    }

                    logger.info("Received packet: " + packet.getOpcode());
                }

                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            logger.info("Connection lost..");
        } catch (IOException e) {
            e.printStackTrace();
            logger.info("Attempting to re-establish.");
            run();
            //System.exit(255);
        }
    }

    public void writePacket(OutgoingPacket packet) {
        logger.info("Wrote packet: " + packet.getOpcode());
        try {
            if (socket.isConnected()) {
                packet.prepare(); // Prepare zeh meal
                byte[] buffer = packet.getPayload();
                dos.write(buffer, 0, buffer.length);
                dos.flush();
            } else {
                run();
                writePacket(packet);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
