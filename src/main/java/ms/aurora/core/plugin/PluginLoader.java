package ms.aurora.core.plugin;

import ms.aurora.api.plugin.Plugin;
import ms.aurora.core.model.PluginSource;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import static com.google.common.collect.Lists.newArrayList;

/**
 * @author rvbiljouw
 */
public class PluginLoader {
    private static final Logger logger = Logger.getLogger(PluginLoader.class);

    private PluginLoader() { }

    public static List<Plugin> getPlugins() {
        List<Plugin> plugins = newArrayList();
        for (PluginSource sourceObj : PluginSource.getAll()) {
            File sourceDirectory = new File(sourceObj.getSource());
            for (File file : sourceDirectory.listFiles()) {
                if (!file.getName().endsWith(".jar")) continue;

                try {
                    URLClassLoader jarClassLoader = new URLClassLoader(new URL[]{
                            new URL("file:" + file.getAbsolutePath())});
                    JarFile jarFile = new JarFile(file);

                    Enumeration<JarEntry> entries = jarFile.entries();
                    while (entries.hasMoreElements()) {
                        JarEntry current = entries.nextElement();
                        String fileName = current.getName();
                        if (!fileName.endsWith(".class")) continue;

                        Plugin plugin = loadPlugin(jarClassLoader, formatClassName(fileName));
                        if (plugin != null) {
                            plugins.add(plugin);
                        }
                    }
                } catch (IOException e) {
                    logger.debug("Failed to load plugin from JAR " + file.getName(), e);
                }
            }
        }
        return plugins;
    }

    private static Plugin loadPlugin(ClassLoader classLoader, String className) {
        try {
            Class<?> pluginClass = classLoader.loadClass(className);
            if (Plugin.class.isAssignableFrom(pluginClass)) {
                Plugin plugin = (Plugin) pluginClass.newInstance();
                if (!plugin.validate()) {
                    throw new IOException("Plugin does not carry a PluginManifest. ( "
                            + className + " )");
                }
                return plugin;
            }
        } catch (ReflectiveOperationException e) {
            logger.debug("Malformed class: " + className, e);
        } catch (IOException e) {
            logger.debug("Failed to load plugin", e);
        }
        return null;
    }

    private static String formatClassName(String fileName) {
        return fileName.replaceAll("/", "\\.").replace(".class", "");
    }

    static {
        if (PluginSource.getBySource("./plugins/").size() == 0) {
            PluginSource local = new PluginSource("./plugins/", false);
            local.save();
        }
    }
}