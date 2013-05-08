package ms.aurora.core.script;

import ms.aurora.api.script.Script;
import ms.aurora.core.model.ScriptSource;
import ms.aurora.sdn.SDNConnection;
import ms.aurora.sdn.net.api.Repository;
import ms.aurora.sdn.net.packet.ScriptRequest;
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
 * @author Rick
 */
public final class ScriptLoader {
    public static List<JarInputStream> remoteStreams = null;

    private static final Logger logger = Logger.getLogger(ScriptLoader.class);

    private ScriptLoader() {
    }

    public static List<Script> getScripts() {
        List<Script> scripts = new ArrayList<Script>();
        Repository.loadScripts();
        scripts.addAll(loadRemoteScripts());
        for (ScriptSource sourceObj : ScriptSource.getAll()) {
            String source = sourceObj.getSource();
            File sourceDirectory = new File(source);
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
                        if (!current.getName().endsWith(".class")) continue;

                        Script script = loadScript(jarClassLoader, formatClassName(fileName));
                        if (script != null) {
                            scripts.add(script);
                        }
                    }
                } catch (IOException e) {
                    logger.debug("Failed to load Script from JAR " + file.getName(), e);
                }
            }
        }
        return scripts;
    }

    private static synchronized List<Script> loadRemoteScripts() {
        List<Script> scripts = new ArrayList<Script>();
        if (remoteStreams != null) {
            JarInputStreamClassLoader classLoader = new JarInputStreamClassLoader(Thread.currentThread().getContextClassLoader(), remoteStreams);
            for (String name : classLoader.getClassNames()) {
                Script script = loadScript(classLoader, name);
                if (script != null) {
                    scripts.add(script);
                }
            }
        }
        return scripts;
    }

    private static Script loadScript(ClassLoader classLoader, String className) {
        try {
            Class<?> scriptClass = classLoader.loadClass(className);
            if (Script.class.isAssignableFrom(scriptClass)) {
                Script script = (Script) scriptClass.newInstance();
                if (!script.validate()) {
                    throw new IOException("Script does not carry a ScriptManifest. ( " + className + " )");
                }
                return script;
            }
        } catch (ReflectiveOperationException e) {
            logger.debug("Malformed class: " + className, e);
        } catch (IOException e) {
            logger.debug("Failed to load script", e);
        }
        return null;
    }

    private static String formatClassName(String fileName) {
        return fileName.replaceAll("/", "\\.").replace(".class", "");
    }

    static {
        if (ScriptSource.getBySource("./scripts/").size() == 0) {
            ScriptSource local = new ScriptSource("./scripts/", false);
            local.save();
        }
    }
}
