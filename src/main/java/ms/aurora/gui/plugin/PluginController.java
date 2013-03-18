package ms.aurora.gui.plugin;

import ms.aurora.api.plugin.Plugin;
import ms.aurora.core.Session;
import ms.aurora.core.SessionRepository;
import ms.aurora.core.model.PluginConfig;

/**
 * @author rvbiljouw
 */
public class PluginController {

    public static void onPluginOverview() {
        PluginOverview overview = new PluginOverview();
        overview.setVisible(true);
    }

    protected static void enablePlugin(Plugin plugin) {
        PluginConfig config = PluginConfig.getByName(
                plugin.getClass().getName());
        config.setEnabled(true);
        config.update();

        updateSessions();
    }

    protected static void disablePlugin(Plugin plugin) {
        PluginConfig config = PluginConfig.getByName(
                plugin.getClass().getName());
        config.setEnabled(false);
        config.update();

        updateSessions();
    }

    protected static void updateSessions() {
        for (Session session : SessionRepository.getAll()) {
            session.refreshPlugins();
        }
    }
}
