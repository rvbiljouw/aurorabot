package ms.aurora.core.script;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import ms.aurora.api.script.LoopScript;
import ms.aurora.api.script.ScriptMetadata;
import org.apache.log4j.Logger;

import java.io.File;
import java.io.FilenameFilter;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.List;
import java.util.Map;

/**
 * A quick and dirty local script loader..
 *
 * @author rvbiljouw
 */
public class ScriptLoader {
    private final static Logger logger = Logger.getLogger(ScriptLoader.class);
    private final List<ScriptMetadata> metadatas = Lists.newArrayList();
    private final Map<ScriptMetadata, LoopScript> scriptMap = Maps.newHashMap();
    private String path = "./scripts/";

    public ScriptLoader(String path) {
        this.path = path;
        reload();
    }

    public ScriptLoader() {
        reload();
    }

    public void reload() {
        try {
            URLClassLoader classLoader = new URLClassLoader(
                    new URL[]{new URL("file:" + path)});
            File scriptRoot = new File(path);

            for (File file : scriptRoot.listFiles(CLASS_FILTER)) {
                Class<?> clazz = classLoader.loadClass(file.getName().replace(
                        ".class", ""));
                if (clazz.isAnnotationPresent(ScriptMetadata.class)
                        && LoopScript.class.isAssignableFrom(clazz)) {
                    ScriptMetadata metadata = clazz
                            .getAnnotation(ScriptMetadata.class);
                    LoopScript script = (LoopScript) clazz.newInstance();
                    scriptMap.put(metadata, script);
                    metadatas.add(metadata);
                    logger.info("Loaded " + metadata.name());
                } else {
                    logger.info("Invalid script " + file.getName()
                            + ", does it have ScriptMetadata?");
                }
            }
        } catch (Exception e) {
           logger.error("Failed to load scripts", e);
        }
    }

    public List<ScriptMetadata> getMetadatas() {
        return metadatas;
    }

    public LoopScript getScript(ScriptMetadata key) {
        return scriptMap.get(key);
    }

    private static final FilenameFilter CLASS_FILTER = new FilenameFilter() {
        @Override
        public boolean accept(File dir, String name) {
            return name.endsWith(".class");
        }
    };
}
