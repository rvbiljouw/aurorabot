package ms.aurora.sdn.net.api;


import ms.aurora.sdn.net.api.repository.RemotePlugin;
import ms.aurora.sdn.net.api.repository.RemoteScript;
import ms.aurora.sdn.net.packet.PluginRequest;
import ms.aurora.sdn.net.packet.ScriptRequest;

import java.util.ArrayList;
import java.util.List;

import static ms.aurora.sdn.SDNConnection.instance;

/**
 * @author tobiewarburton
 */
public class Repository {
    public static final Object script_lock = new Object();
    public static final Object plugin_lock = new Object();

    public static final List<RemoteScript> REMOTE_SCRIPT_LIST = new ArrayList<RemoteScript>();
    public static final List<RemotePlugin> REMOTE_PLUGIN_LIST = new ArrayList<RemotePlugin>();

    public static byte[] store;

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
