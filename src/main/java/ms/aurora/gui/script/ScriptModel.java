package ms.aurora.gui.script;

import javafx.beans.property.SimpleStringProperty;
import ms.aurora.api.script.Script;
import ms.aurora.api.script.ScriptManifest;

/**
 * @author rvbiljouw
 */
public class ScriptModel {
    protected final ScriptManifest manifest;
    protected final Class<? extends Script> script;
    private SimpleStringProperty name;
    private SimpleStringProperty shortDesc;
    private SimpleStringProperty category;
    private SimpleStringProperty author;

    public ScriptModel(Class<? extends Script> script, ScriptManifest manifest) {
        this.script = script;
        this.manifest = manifest;

        this.name = new SimpleStringProperty(manifest.name());
        this.shortDesc = new SimpleStringProperty(manifest.shortDescription());
        this.category = new SimpleStringProperty(manifest.category());
        this.author = new SimpleStringProperty(manifest.author());
    }

    public String getName() {
        return name.getValue();
    }

    public String getShortDesc() {
        return shortDesc.getValue();
    }

    public String getCategory() {
        return category.getValue();
    }

    public String getAuthor() {
        return author.getValue();
    }
}
