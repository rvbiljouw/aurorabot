package ms.aurora.sdn.net.api;

import ms.aurora.sdn.SDNConnection;
import ms.aurora.sdn.net.packet.MapDataRequest;

/**
 * @author rvbiljouw
 */
public class Maps {

    public static void obtainMap() {
        SDNConnection.getInstance().writePacket(new MapDataRequest());
    }

}
