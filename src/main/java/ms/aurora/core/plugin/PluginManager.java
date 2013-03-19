package ms.aurora.core.plugin;

import ms.aurora.api.plugin.Plugin;
import ms.aurora.api.plugin.PluginState;
import ms.aurora.core.Session;
import org.apache.log4j.Logger;

import java.util.Map;

import static com.google.common.collect.Maps.newHashMap;

/**
 * @author rvbiljouw
 */
public final class PluginManager {
    private static final Logger logger = Logger.getLogger(PluginManager.class);
    private final Map<Class<? extends Plugin>, Plugin> pluginMap = newHashMap();
    private final Session session;

    public PluginManager(Session session) {
        this.session = session;
    }

    public void start(Class<? extends Plugin> pluginClass) {
        try {
            if (!pluginMap.containsKey(pluginClass)) {
                Plugin plugin = pluginClass.newInstance();
                plugin.setSession(session);
                plugin.setState(PluginState.INIT);
                pluginMap.put(pluginClass, plugin);
            }
        } catch (Exception e) {
            logger.error("Failed to initialize plugin", e);
        }
    }

    public void stop(Class<? extends Plugin> pluginClass) {
        if (pluginMap.containsKey(pluginClass)) {
            Plugin plugin = pluginMap.get(pluginClass);
            plugin.setState(PluginState.STOPPED);
            pluginMap.remove(pluginClass);
        }
    }

}
