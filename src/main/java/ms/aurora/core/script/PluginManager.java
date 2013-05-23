package ms.aurora.core.script;

import ms.aurora.api.plugin.Plugin;
import ms.aurora.api.plugin.PluginState;
import ms.aurora.core.Session;
import ms.aurora.core.model.PluginConfig;
import org.apache.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * A class responsible for the management of plugins.
 * Plugins can be started and stopped.
 *
 * @author Rick
 */
public final class PluginManager {
    private static final Logger logger = Logger.getLogger(PluginManager.class);
    private final Map<String, Plugin> pluginMap = new HashMap<>();
    private final Session session;

    public PluginManager(Session session) {
        this.session = session;
    }

    public void start(Class<? extends Plugin> pluginClass) {
        try {
            if (!pluginMap.containsKey(pluginClass.getName())) {
                Plugin plugin = pluginClass.newInstance();
                plugin.setState(PluginState.INIT);
                pluginMap.put(pluginClass.getName(), plugin);
            }
        } catch (Exception e) {
            logger.error("Failed to initialize plugin", e);
        }
    }

    public void stop(Class<? extends Plugin> pluginClass) {
        try {
            if (pluginMap.containsKey(pluginClass.getName())) {
                Plugin plugin = pluginMap.get(pluginClass.getName());
                plugin.setState(PluginState.STOPPED);
                pluginMap.remove(pluginClass.getName());
            }
        } catch (Exception e) {
            logger.error("Failed to stop plugin", e);
        }
    }

    public void stop() {
        for (Class<? extends Plugin> plugin : EntityLoader.getPlugins()) {
            stop(plugin);
        }
    }

    public void refresh() {
        new Thread(session.getThreadGroup(), new Runnable() {
            @Override
            public void run() {
                for (Class<? extends Plugin> plugin : EntityLoader.getPlugins()) {
                    PluginConfig config = PluginConfig.getByName(
                            plugin.getName());
                    stop(plugin);
                    if (config.isEnabled()) {
                        start(plugin);
                    } else {
                        stop(plugin);
                    }
                }
            }
        }).start();
    }
}
