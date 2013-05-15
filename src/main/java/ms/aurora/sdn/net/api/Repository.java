package ms.aurora.sdn.net.api;


import ms.aurora.sdn.net.packet.PluginRequest;
import ms.aurora.sdn.net.packet.ScriptCountRequest;
import ms.aurora.sdn.net.packet.ScriptRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.jar.JarInputStream;

import static ms.aurora.sdn.SDNConnection.instance;

/**
 * @author tobiewarburton
 */
public class Repository {
    public static final Object script_lock = new Object();
    public static final Object script_count_lock = new Object();
    public static final Object plugin_lock = new Object();
    public static int REMOTE_SCRIPT_COUNT = -1;

    public static List<JarInputStream> remoteScriptStreams = new ArrayList<JarInputStream>();
    public static List<JarInputStream> remotePluginStreams = new ArrayList<JarInputStream>();

    public static void loadScripts() {
        synchronized (script_count_lock) {
            instance.writePacket(new ScriptCountRequest());
            try {
                script_count_lock.wait(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            if (REMOTE_SCRIPT_COUNT == -1) {
                throw new IllegalStateException();
            }

            remoteScriptStreams = new ArrayList<JarInputStream>();
            for (int i = 0; i < REMOTE_SCRIPT_COUNT; i++) {
                synchronized (script_lock) {
                    instance.writePacket(new ScriptRequest(i));
                    try {
                        script_lock.wait(10000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();  //To change body of catch statement use File | Settings | File Templates.
                    }
                }
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
