package ms.aurora.core;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ms.aurora.api.plugin.Plugin;
import ms.aurora.core.model.PluginConfig;
import ms.aurora.core.plugin.PluginLoader;
import ms.aurora.core.plugin.PluginManager;
import ms.aurora.core.script.ScriptManager;
import ms.aurora.event.PaintManager;
import ms.aurora.gui.plugin.PluginOverview;

import javax.swing.*;
import java.applet.Applet;

/**
 * @author rvbiljouw
 */
public final class Session implements Runnable {
    private final ScriptManager scriptManager = new ScriptManager(this);
    private final PluginManager pluginManager = new PluginManager(this);
    private final PaintManager paintManager = new PaintManager(this);
    private final Menu pluginsMenu = new Menu("Plug-ins");
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

    public void registerMenu(final Menu menu) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                pluginsMenu.getItems().add(menu);
            }
        });
    }

    public void deregisterMenu(final Menu menu) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                pluginsMenu.getItems().remove(menu);
            }
        });
    }

    public void injectPluginMenu(Menu menu) {
        menu.getItems().addAll(pluginsMenu.getItems());
    }

    public void pullOutPluginMenu(Menu menu) {
        menu.getItems().removeAll(pluginsMenu.getItems());
    }

    public Menu getPluginMenu() {
        return pluginsMenu;
    }
}
