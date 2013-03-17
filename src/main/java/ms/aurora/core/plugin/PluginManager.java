package ms.aurora.core.plugin;

import ms.aurora.api.plugin.Plugin;
import ms.aurora.api.plugin.PluginState;
import ms.aurora.core.Session;

import java.util.List;

import static com.google.common.collect.Lists.newArrayList;

/**
 * @author rvbiljouw
 */
public class PluginManager {
    private final List<Plugin> plugins = newArrayList();
    private final Session session;

    public PluginManager(Session session) {
        this.session = session;
    }

    public void start(Plugin plugin) {
        plugin.setSession(session);
        plugin.setState(PluginState.INIT);
        plugins.add(plugin);
    }

    public void stop(Plugin plugin) {
        plugin.setState(PluginState.STOPPED);
        plugins.remove(plugin);
    }
}
