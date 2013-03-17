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
public class ScriptSettings {
    private static final String PATH = System.getProperty("user.home") + "/.aurora/scriptconf.json";
    private static final JSONDeserializer DESERIALIZER = new JSONDeserializer<ScriptSettings>();
    private static final JSONSerializer SERIALIZER = new JSONSerializer();
    private static final Logger logger = getLogger(ScriptSettings.class);
    private static ScriptSettings instance;

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
            logger.error("Failed to persist the script settings", e);
        }
    }

    public static ScriptSettings get() {
        if (instance == null) {
            try {
                String path = System.getProperty("user.home") + "/.aurora/scriptconf.json";
                instance = (ScriptSettings) DESERIALIZER.deserialize(new FileReader(path));
            } catch (FileNotFoundException e) {
                ScriptSettings newSettings = new ScriptSettings();
                newSettings.addSource("./scripts/");
            }
        }
        return instance;
    }
}
