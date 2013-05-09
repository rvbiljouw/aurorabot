package ms.aurora.sdn.net.api;


import ms.aurora.sdn.net.packet.PluginRequest;
import ms.aurora.sdn.net.packet.ScriptRequest;

import static ms.aurora.sdn.SDNConnection.instance;

/**
 * @author tobiewarburton
 */
public class Repository {
    public static final Object script_lock = new Object();
    public static final Object plugin_lock = new Object();

    public static void loadScripts() {
        synchronized (script_lock) {
            instance.writePacket(new ScriptRequest());
            try {
                script_lock.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void loadPlugins() {
        synchronized (plugin_lock) {
            instance.writePacket(new PluginRequest());
            try {
                plugin_lock.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
