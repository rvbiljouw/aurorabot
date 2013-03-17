package ms.aurora.core.plugin;

import ms.aurora.api.plugin.Plugin;
import ms.aurora.settings.PluginSettings;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
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
    private final List<Plugin> plugins = newArrayList();

    public PluginLoader() {

    }

    public void load() {
        PluginSettings settings = PluginSettings.get();
        for (String source : settings.getSources()) {
            try {
                URLClassLoader classLoader = new URLClassLoader(new URL[]{new URL("file:" + source)});
                File sourceDirectory = new File(source);
                for (File file : sourceDirectory.listFiles()) {
                    if (file.getName().endsWith(".jar")) {
                        URLClassLoader jarClassLoader = new URLClassLoader(new URL[]{
                                new URL("file:" + file.getAbsolutePath())});
                        try {
                            JarFile jarFile = new JarFile(file);
                            Enumeration<JarEntry> entries = jarFile.entries();
                            while (entries.hasMoreElements()) {
                                JarEntry current = entries.nextElement();
                                if (current.getName().endsWith(".class")) {
                                    loadPlugin(jarClassLoader, source, current.getName().replaceAll("/", "\\.").
                                            replace(".class", ""));
                                }
                            }
                        } catch (IOException e) {
                            logger.error("Failed to load plugin from JAR " + file.getName(), e);
                        }
                    } else if (file.getName().endsWith(".class")) {
                        loadPlugin(classLoader, source, file.getName().replace(".class", ""));
                    }
                }

            } catch (MalformedURLException e) {
                logger.error("Malformed source in PluginSettings", e);
            }
        }
    }

    private void loadPlugin(ClassLoader classLoader, String source, String className) {
        try {
            Class<?> pluginClass = classLoader.loadClass(className);
            Object instance = pluginClass.newInstance();
            if (instance instanceof Plugin) {
                Plugin plugin = (Plugin) instance;
                if (!plugin.validate()) {
                    throw new IOException("Plugin does not carry a PluginManifest.");
                }
                plugins.add(plugin);
            }
        } catch (ClassNotFoundException e) {
            logger.error("Malformed class in source " + source, e);
        } catch (InstantiationException e) {
            logger.error("Malformed class in source " + source, e);
        } catch (IllegalAccessException e) {
            logger.error("Malformed class in source " + source, e);
        } catch (IOException e) {
            logger.error("Failed to load plugin", e);
        }
    }

    public List<Plugin> getPlugins() {
        return plugins;
    }
}
