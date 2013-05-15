package ms.aurora.core.entity;

import ms.aurora.api.plugin.Plugin;
import ms.aurora.api.plugin.internal.InterfacePlugin;
import ms.aurora.api.plugin.internal.PaintDebug;
import ms.aurora.api.plugin.internal.TileUtilities;
import ms.aurora.api.random.Random;
import ms.aurora.api.script.Script;
import ms.aurora.api.util.Predicate;
import ms.aurora.core.entity.exception.ManifestException;
import ms.aurora.core.model.Source;
import ms.aurora.sdn.net.api.Repository;
import ms.aurora.util.JarInputStreamClassLoader;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.jar.JarInputStream;

/**
 * @author tobiewarburton
 */
public class EntityLoader<T> {
    private final static List<Plugin> PLUGINS = Arrays.asList(new Plugin[]{
            new InterfacePlugin(),
            new TileUtilities(),
            new PaintDebug(),
    });

    public static EntityLoader<Plugin> pluginEntityLoader;
    public static EntityLoader<Script> scriptEntityLoader;
    public static EntityLoader<Random> randomEntityLoader;

    private static final Logger logger = Logger.getLogger(EntityLoader.class);

    private Class<T> type;
    private Predicate<T> validator;
    private List<T> internal;

    private EntityLoader(Class<T> type, Predicate<T> validator, List<T> internal) {
        this.type = type;
        this.validator = validator;
        this.internal = internal;
    }

    private EntityLoader(Class<T> type, Predicate<T> validator) {
        this.type = type;
        this.validator = validator;
        this.internal = new ArrayList<T>();
    }

    public List<T> getEntitys() {
        List<T> entityList = new ArrayList<T>();
        entityList.addAll(internal);
        //entityList.addAll(loadRemote());
        for (Source source : Source.getAll()) {
            File sourceDirectory = new File(source.getSource());
            if (!sourceDirectory.exists()) {
                sourceDirectory.mkdirs();
            }
            for (File file : sourceDirectory.listFiles()) {
                if (!file.getName().endsWith(".jar")) continue;

                try {
                    URLClassLoader jarClassLoader = new URLClassLoader(new URL[]{
                            new URL("file:" + file.getAbsolutePath())},
                            Thread.currentThread().getContextClassLoader());
                    JarFile jarFile = new JarFile(file);

                    Enumeration<JarEntry> entries = jarFile.entries();
                    while (entries.hasMoreElements()) {
                        JarEntry current = entries.nextElement();
                        String fileName = current.getName();
                        if (!fileName.endsWith(".class")) continue;

                        T entity = loadEntity(jarClassLoader, formatClassName(fileName));
                        if (entity != null) {
                            entityList.add(entity);
                        }
                    }
                } catch (IOException e) {
                    logger.debug("Failed to load entity from JAR " + file.getName(), e);
                }
            }
        }
        return entityList;
    }

    private T loadEntity(ClassLoader classLoader, String className) {
        try {
            Class<?> entityClass = classLoader.loadClass(className);
            if (type.isAssignableFrom(entityClass)) {
                T entity = (T) entityClass.newInstance();
                if (!validator.apply(entity)) {
                    throw new ManifestException("Entity does not carry a Manifest. ( " + className + " )");
                }
                return entity;
            }
        } catch (ReflectiveOperationException e) {
            logger.debug("Malformed class: " + className, e);
        } catch (Exception e) {
            logger.error("Failed to load entity", e);
        }
        return null;
    }

    private List<T> loadRemote() {
        List<T> entityList = new ArrayList<T>();
        List<JarInputStream> remoteStreams = null;
        if (type == Script.class) {
            remoteStreams = Repository.remoteScriptStreams;
        } else if (type == Plugin.class) {
            remoteStreams = Repository.remotePluginStreams;
        }
        if (remoteStreams != null) {
            JarInputStreamClassLoader classLoader = new JarInputStreamClassLoader(Thread.currentThread().getContextClassLoader(),
                    remoteStreams);
            for (String name : classLoader.getClassNames()) {
                try {
                    T entity = loadEntity(classLoader, name);
                    if (entity != null) {
                        entityList.add(entity);
                    }
                } catch (Exception e) {
                    logger.error("Failed to load entity", e);
                }
            }
        }
        return entityList;
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
        if (Source.getBySource("./plugins/").size() == 0) {
            Source local = new Source("./plugins/", false);
            local.save();
        }
        if (Source.getBySource("./scripts/").size() == 0) {
            Source local = new Source("./scripts/", false);
            local.save();
        }
        if (Source.getBySource("./randoms/").size() == 0) {
            Source local = new Source("./randoms/", false);
            local.save();
        }
        for (Source source : Source.getAll()) {
            File dir = new File(source.getSource());
            if (!dir.exists()) {
                dir.mkdirs();
            }
        }

        pluginEntityLoader = new EntityLoader<Plugin>(Plugin.class, new Predicate<Plugin>() {
            @Override
            public boolean apply(Plugin object) {
                return object.getManifest() != null;
            }
        }, PLUGINS);
        scriptEntityLoader = new EntityLoader<Script>(Script.class, new Predicate<Script>() {
            @Override
            public boolean apply(Script object) {
                return object.getManifest() != null;
            }
        });
        randomEntityLoader = new EntityLoader<Random>(Random.class, new Predicate<Random>() {
            @Override
            public boolean apply(Random object) {
                return object.getManifest() != null;
            }
        });
    }
}
