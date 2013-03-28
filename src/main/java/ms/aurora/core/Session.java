package ms.aurora.core;

import java.applet.Applet;

import javax.swing.JMenu;
import javax.swing.SwingUtilities;

import ms.aurora.api.plugin.Plugin;
import ms.aurora.core.model.PluginConfig;
import ms.aurora.core.plugin.PluginLoader;
import ms.aurora.core.plugin.PluginManager;
import ms.aurora.core.script.ScriptManager;
import ms.aurora.event.PaintManager;

/**
 * @author rvbiljouw
 */
public final class Session implements Runnable {
    private final ScriptManager scriptManager = new ScriptManager(this);
    private final PluginManager pluginManager = new PluginManager(this);
    private final PaintManager paintManager = new PaintManager(this);
    private final JMenu pluginsMenu = new JMenu("Plug-ins");
    private final Applet applet;

    public Session(Applet applet) {
        this.applet = applet;
    }

    @Override
    public void run() {
        SessionRepository.set(getApplet().hashCode(), this);
    }

    public ScriptManager getScriptManager() {
        return scriptManager;
    }

    public PluginManager getPluginManager() {
        return pluginManager;
    }

    public PaintManager getPaintManager() {
        return paintManager;
    }

    public JMenu getTools() {
        return pluginsMenu;
    }

    public Applet getApplet() {
        return applet;
    }

    public void refreshPlugins() {
        for (Plugin plugin : PluginLoader.getPlugins()) {
            PluginConfig config = PluginConfig.getByName(
                    plugin.getClass().getName());

            if (config.isEnabled()) {
                pluginManager.start(plugin.getClass());
            } else {
                pluginManager.stop(plugin.getClass());
            }
        }
    }

    public void registerMenu(final JMenu menu) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                pluginsMenu.add(menu);
            }
        });
    }

    public void deregisterMenu(final JMenu menu) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                pluginsMenu.remove(menu);
            }
        });
    }

}
