package ms.aurora.sdn.net.api;

import ms.aurora.browser.wrapper.Plaintext;
import ms.aurora.sdn.SDNConnection;
import ms.aurora.sdn.net.packet.ClientDataRequest;

/**
 * @author rvbiljouw
 */
public class ClientData {
    private static final Object dataLock = new Object();
    private static Plaintext data;

    public static void obtainData() {
        SDNConnection.getInstance().writePacket(new ClientDataRequest());
        synchronized(dataLock) {
            try {
                dataLock.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
            }
        }
    }

    public static synchronized Plaintext getData() {
        return data;
    }

    public static synchronized void setData(Plaintext plaintext) {
        data = plaintext;
        synchronized(dataLock) {
            dataLock.notifyAll();
        }
    }
}
