package ms.aurora.sdn.net.api;

import ms.aurora.sdn.SDNConnection;
import ms.aurora.sdn.net.packet.HookRequest;
import ms.aurora.transform.ClientDefinition;

/**
 * @author rvbiljouw
 */
public class Hooks {
    private static ClientDefinition definition;

    public static void obtainHooks() {
        SDNConnection.getInstance().writePacket(new HookRequest());
    }

    public static synchronized ClientDefinition getHooks() {
        return definition;
    }

    public static synchronized void setHooks(ClientDefinition aDefinition) {
        System.out.println("Set hooks");
        definition = aDefinition;
    }
}
