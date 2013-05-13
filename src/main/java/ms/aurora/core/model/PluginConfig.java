package ms.aurora.core.model;

import com.avaje.ebean.Ebean;

import javax.persistence.*;
import java.util.List;

import static com.avaje.ebean.Ebean.find;

/**
 * Database-controlled plugin configuration so
 * we can restart previously enabled plugins
 * when the bot starts.
 * @author Rick
 */
@Entity
public class PluginConfig extends AbstractModel {

    @Id
    @GeneratedValue
    private Long id;
    private String pluginMain;
    private boolean enabled;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPluginMain() {
        return pluginMain;
    }

    public void setPluginMain(String pluginMain) {
        this.pluginMain = pluginMain;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public static PluginConfig getByName(String pluginMain) {
        List<PluginConfig> results = find(PluginConfig.class).where().eq(
                "pluginMain", pluginMain).findList();

        if (results.size() == 1) {
            return results.get(0);
        } else {
            // If it doesn't exist, create it.
            PluginConfig config = new PluginConfig();
            config.setPluginMain(pluginMain);
            config.setEnabled(false);
            config.save();
            return config;
        }
    }

    public static List<PluginConfig> getAll() {
        return Ebean.find(PluginConfig.class).findList();
    }
}
