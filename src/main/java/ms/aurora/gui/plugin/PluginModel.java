package ms.aurora.gui.plugin;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import ms.aurora.api.plugin.Plugin;
import ms.aurora.api.plugin.PluginManifest;
import ms.aurora.core.model.PluginConfig;

import static ms.aurora.core.model.PluginConfig.getByName;

/**
 * @author rvbiljouw
 */
public class PluginModel {
    protected final PluginManifest manifest;
    private SimpleStringProperty name;
    private SimpleStringProperty shortDesc;
    private SimpleStringProperty author;
    private SimpleBooleanProperty state;

    public PluginModel(PluginManifest manifest) {
        PluginConfig config = getByName(manifest.name());
        this.manifest = manifest;
        this.name = new SimpleStringProperty(manifest.name());
        this.shortDesc = new SimpleStringProperty(manifest.shortDescription());
        this.author = new SimpleStringProperty(manifest.author());
        this.state = new SimpleBooleanProperty(config.isEnabled());
    }

    public String getName() {
        return name.getValue();
    }

    public String getShortDesc() {
        return shortDesc.getValue();
    }

    public String getAuthor() {
        return author.getValue();
    }

    public Boolean getState() {
        return state.getValue();
    }

    public void setState(boolean state) {
        this.state.setValue(state);
    }
}
