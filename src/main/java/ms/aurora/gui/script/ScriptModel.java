package ms.aurora.gui.script;

import javafx.beans.property.SimpleStringProperty;
import ms.aurora.api.script.ScriptManifest;

/**
 * @author rvbiljouw
 */
public class ScriptModel {
    protected final ScriptManifest manifest;
    private SimpleStringProperty name;
    private SimpleStringProperty shortDesc;
    private SimpleStringProperty category;
    private SimpleStringProperty author;

    public ScriptModel(ScriptManifest manifest) {
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
