package ms.aurora.core;

import ms.aurora.api.plugin.Plugin;
import ms.aurora.core.model.PluginConfig;
import ms.aurora.core.plugin.PluginLoader;
import ms.aurora.core.plugin.PluginManager;
import ms.aurora.core.script.ScriptManager;
import ms.aurora.event.PaintManager;
import ms.aurora.gui.plugin.PluginController;

import javax.swing.*;
import java.applet.Applet;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                JMenuItem mntmPluginOverview = new JMenuItem("Plugin Overview");
                mntmPluginOverview.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        PluginController.onPluginOverview();
                    }
                });
                pluginsMenu.add(mntmPluginOverview);
            }
        });
        refreshPlugins();
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
                System.out.println("Starting plugin " + config.getPluginMain());
            } else {
                pluginManager.stop(plugin.getClass());
                System.out.println("Stopping plugin " + config.getPluginMain());
            }
        }
    }

    public void registerMenu(JMenu menu) {
        pluginsMenu.add(menu);
    }

    public void deregisterMenu(JMenu menu) {
        pluginsMenu.remove(menu);
    }

}
