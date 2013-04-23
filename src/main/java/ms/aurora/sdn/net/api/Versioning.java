package ms.aurora.sdn.net.api;

import ms.aurora.Application;
import ms.aurora.sdn.SDNConnection;
import ms.aurora.sdn.net.OutgoingPacket;
import ms.aurora.sdn.net.packet.UpdateRequest;

import java.io.File;

/**
 * @author rvbiljouw
 */
public class Versioning {
    public static final String PATH = Versioning.class.getProtectionDomain().getCodeSource().getLocation().getFile();
    public static final File FILE = new File(PATH);

    public static void checkForUpdates() {
        if (!PATH.contains(".jar") || !FILE.exists()) {
            Application.logger.info("Path issues, not updating..");
        } else {
            OutgoingPacket updatePacket = new UpdateRequest(FILE);
            SDNConnection.getInstance().writePacket(updatePacket);
        }
    }

}
