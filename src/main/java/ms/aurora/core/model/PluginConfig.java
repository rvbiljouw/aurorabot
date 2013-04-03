package ms.aurora.core.model;

import javax.persistence.*;
import java.util.List;

/**
 * @author Rick
 */
@Entity
@NamedQueries({
        @NamedQuery(name = "pluginConfig.getByName",
                query = "select p from PluginConfig p where p.pluginMain = :pluginMain"),
        @NamedQuery(name = "pluginConfig.getAll",
                query = "select p from PluginConfig p")
})
public final class PluginConfig extends AbstractModel {

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
        TypedQuery<PluginConfig> query = getEm().createNamedQuery("pluginConfig.getByName",
                PluginConfig.class).setParameter("pluginMain", pluginMain);
        List<PluginConfig> results = query.getResultList();

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
        return getEm().createNamedQuery("pluginConfig.getAll", PluginConfig.class).getResultList();
    }
}
