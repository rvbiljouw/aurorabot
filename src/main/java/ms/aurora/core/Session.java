package ms.aurora.core;

import ms.aurora.api.plugin.Plugin;
import ms.aurora.core.model.PluginConfig;
import ms.aurora.core.plugin.PluginLoader;
import ms.aurora.core.plugin.PluginManager;
import ms.aurora.core.script.ScriptManager;
import ms.aurora.event.PaintManager;

import java.applet.Applet;
import java.util.Map;

import static com.google.common.collect.Maps.newHashMap;

/**
 * @author rvbiljouw
 */
public class Session implements Runnable {
    private final static Map<String, Object> settings = newHashMap();
    private final ScriptManager scriptManager = new ScriptManager(this);
    private final PluginManager pluginManager = new PluginManager(this);
    private final PaintManager paintManager = new PaintManager(this);
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
        System.out.println("Refreshing plugins");
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

    public Object get(String key) {
        return settings.get(key);
    }

    public void set(String key, Object object) {
        settings.put(key, object);
    }


}
