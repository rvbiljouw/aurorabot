package ms.aurora.settings;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;
import org.apache.log4j.Logger;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

import static org.apache.log4j.Logger.getLogger;

/**
 * @author rvbiljouw
 */
public class PluginSettings {
    private static final String PATH = System.getProperty("user.home") + "/.aurora/pluginconf.json";
    private static final JSONDeserializer DESERIALIZER = new JSONDeserializer();
    private static final JSONSerializer SERIALIZER = new JSONSerializer();
    private static final Logger logger = getLogger(ScriptSettings.class);
    private static PluginSettings instance;

    private List<String> sources = new ArrayList<String>();

    public void addSource(String source) {
        sources.add(source);
    }

    public void removeSource(String source) {
        sources.remove(source);
    }

    public void clear() {
        sources.clear();
    }

    public void reload() {
        instance = null;
        instance = get();
    }

    public void persist() {
        try {
            File file = new File(PATH);
            if (!file.exists()) {
                file.createNewFile();
            }
            SERIALIZER.serialize(this, new FileWriter(file, false));
        } catch (IOException e) {
            logger.error("Failed to persist the plugin settings", e);
        }
    }

    public static PluginSettings get() {
        if (instance == null) {
            try {
                instance = (PluginSettings) DESERIALIZER.deserialize(new FileReader(PATH));
            } catch (FileNotFoundException e) {
                ScriptSettings newSettings = new ScriptSettings();
                newSettings.addSource("./scripts/");
            }
        }
        return instance;
    }

    public List<String> getSources() {
        return sources;
    }
}
