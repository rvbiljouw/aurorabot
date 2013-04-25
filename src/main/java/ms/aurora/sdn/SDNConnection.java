package ms.aurora.sdn;

import jfx.messagebox.MessageBox;
import ms.aurora.Application;
import ms.aurora.sdn.net.IncomingPacket;
import ms.aurora.sdn.net.OutgoingPacket;
import ms.aurora.sdn.net.PacketHandler;
import ms.aurora.sdn.net.impl.LoginPacketHandler;
import ms.aurora.sdn.net.impl.UpdatePacketHandler;
import org.apache.log4j.Logger;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Proxy;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import static org.apache.log4j.Logger.getLogger;

/**
 * @author rvbiljouw
 */
public class SDNConnection implements Runnable {
    private static final SDNConnection instance = new SDNConnection();
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
            packetHandlers.add(new LoginPacketHandler());
            packetHandlers.add(new UpdatePacketHandler());

            logger.info("Attempting to connect.");
            socket = new Socket("208.94.241.76", 443);
            socket.setKeepAlive(true);
            dis = new DataInputStream(socket.getInputStream());
            dos = new DataOutputStream(socket.getOutputStream());
            logger.info("Connection established.");
            while (socket.isConnected() && !self.isInterrupted()) {
                if (dis.available() > 0) {
                    IncomingPacket packet = new IncomingPacket(dis.readInt(), dis);
                    logger.info("Received " + packet.getOpcode());

                    for (PacketHandler handler : packetHandlers) {
                        if (handler.getOpcode() == packet.getOpcode()) {
                            handler.handle(packet);
                            break;
                        }
                    }
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
            System.exit(255);
        }
    }

    public void writePacket(OutgoingPacket packet) {
        try {
            long time = System.currentTimeMillis();
            while ((dos == null || !socket.isConnected()) &&
                    (System.currentTimeMillis() - time) <= 20000) {
                Thread.sleep(1000);
            }

            if (dos == null || !socket.isConnected()) {
                MessageBox.show(Application.mainStage, "We couldn't make a connection to the dashboard.\n" +
                        "If you think this is wrong, please make a post on the forums.",
                        "Connection error!", MessageBox.OK);
                System.exit(0);
            }

            packet.prepare(); // Prepare zeh meal
            byte[] buffer = packet.getPayload();
            dos.write(buffer, 0, buffer.length);
            dos.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
