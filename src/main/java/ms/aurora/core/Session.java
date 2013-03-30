package ms.aurora.core;

import javafx.application.Platform;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import ms.aurora.api.plugin.Plugin;
import ms.aurora.core.model.PluginConfig;
import ms.aurora.core.plugin.PluginLoader;
import ms.aurora.core.plugin.PluginManager;
import ms.aurora.core.script.ScriptManager;
import ms.aurora.event.PaintManager;

import java.applet.Applet;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * @author rvbiljouw
 */
public final class Session implements Runnable {
    private final ScriptManager scriptManager = new ScriptManager(this);
    private final PluginManager pluginManager = new PluginManager(this);
    private final PaintManager paintManager = new PaintManager(this);
    private final CopyOnWriteArrayList<MenuItem> pluginMenu = new CopyOnWriteArrayList<>();
    private UpdateListener updateListener;
    private final Applet applet;

    public Session(Applet applet) {
        this.applet = applet;
    }

    @Override
    public void run() {
        SessionRepository.set(getApplet().hashCode(), this);
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
                pluginMenu.add(menu);
                if(updateListener != null) {
                    updateListener.onUpdate();
                }
                System.out.println("added item");
            }
        });
    }

    public void deregisterMenu(final Menu menu) {
        Platform.runLater(new Runnable() {
            @Override
            public void run() {
                pluginMenu.remove(menu);
                if(updateListener != null) {
                    updateListener.onUpdate();
                }
            }
        });
    }

    public CopyOnWriteArrayList<MenuItem> getPluginMenu() {
        return pluginMenu;
    }

    public void setUpdateListener(UpdateListener updateListener) {
        this.updateListener = updateListener;
    }

    public interface UpdateListener {

        public void onUpdate();

    }
}
