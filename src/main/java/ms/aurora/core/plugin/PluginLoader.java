package ms.aurora.core.plugin;

import ms.aurora.api.plugin.Plugin;
import ms.aurora.api.plugin.internal.InterfacePlugin;
import ms.aurora.api.plugin.internal.PaintDebug;
import ms.aurora.api.plugin.internal.TileUtilities;
import ms.aurora.core.model.PluginSource;
import ms.aurora.core.plugin.exception.ManifestException;
import ms.aurora.core.plugin.exception.PluginException;
import ms.aurora.util.JarInputStreamClassLoader;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarInputStream;

/**
 * A class responsible for the loading of plugins
 *
 * @author Rick
 */
public final class PluginLoader {
    private static final Logger logger = Logger.getLogger(PluginLoader.class);

    public static List<JarInputStream> remoteStreams = null;

    private PluginLoader() {
    }

    public static List<Plugin> getPlugins() {
        List<Plugin> plugins = new ArrayList<Plugin>();
        //Repository.loadPlugins();
        //plugins.addAll(loadRemotePlugins());
        plugins.add(new InterfacePlugin());
        plugins.add(new TileUtilities());
        plugins.add(new PaintDebug());
        for (PluginSource sourceObj : PluginSource.getAll()) {
            File sourceDirectory = new File(sourceObj.getSource());
            if (!sourceDirectory.exists()) {
                sourceDirectory.mkdirs();
            }

            for (File file : sourceDirectory.listFiles()) {
                if (!file.getName().endsWith(".jar")) continue;

                try {
                    URLClassLoader jarClassLoader = new URLClassLoader(new URL[]{
                            new URL("file:" + file.getAbsolutePath())}, Thread.currentThread().getContextClassLoader());
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

    private static synchronized List<Plugin> loadRemotePlugins() {
        List<Plugin> plugins = new ArrayList<Plugin>();
        if (remoteStreams != null) {
            JarInputStreamClassLoader classLoader = new JarInputStreamClassLoader(Thread.currentThread().getContextClassLoader(), remoteStreams);
            for (String name : classLoader.getClassNames()) {
                Plugin plugin = loadPlugin(classLoader, name);
                if (plugin != null) {
                    plugins.add(plugin);
                }
            }
        }
        return plugins;
    }

    /**
     * Loads a plugin with a specified name from a specified class loader.
     *
     * @param classLoader Class loader from which we should obtain the plugin class.
     * @param className   Name of the plugin class
     * @return Loaded plugin or null
     * @throws ManifestException if the manifest isn't available
     * @throws PluginException   if an error occurs instantiating or finding the plugin.
     */
    private static Plugin loadPlugin(ClassLoader classLoader, String className)
            throws ManifestException, PluginException {
        try {
            Class<?> pluginClass = classLoader.loadClass(className);
            if (Plugin.class.isAssignableFrom(pluginClass)) {
                Plugin plugin = (Plugin) pluginClass.newInstance();
                if (!plugin.validate()) {
                    throw new ManifestException("Plugin does not carry a PluginManifest. ( "
                            + className + " )");
                }
                return plugin;
            }
        } catch(ReflectiveOperationException ex) {
            throw new PluginException("Failed to initialize plugin " + className, ex);
        }
        return null;
    }

    /**
     * Ensures the class name is in the expected format for the class loader.
     * Example: ms/aurora/Application.class becomes ms.aurora.Application
     *
     * @param fileName
     * @return formatted class name
     */
    private static String formatClassName(String fileName) {
        return fileName.replaceAll("/", "\\.").replace(".class", "");
    }

    static {
        /**
         * Initial check to see if the default plugin source (localdir) is already added.
         * If it's not there, we add it now.
         */
        if (PluginSource.getBySource("./plugins/").size() == 0) {
            PluginSource local = new PluginSource("./plugins/", false);
            local.save();
        }
    }
}
