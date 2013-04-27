package ms.aurora.core.model;

import javax.persistence.*;
import java.util.List;

/**
 * @author Rick
 */
@Entity
@NamedQueries({
        @NamedQuery(name = "pluginSource.getAll", query = "select s from PluginSource s"),
        @NamedQuery(name = "pluginSource.getBySource", query = "select s from PluginSource s where source = :source")
})
public final class PluginSource extends AbstractModel {

    @Id
    @GeneratedValue
    private Long id;
    private String source;
    private boolean devMode;

    public PluginSource(String source, boolean devMode) {
        this.source = source;
        this.devMode = devMode;
    }

    public PluginSource() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public boolean isDevMode() {
        return devMode;
    }

    public void setDevMode(boolean devMode) {
        this.devMode = devMode;
    }

    @Override
    public String toString() {
        return source;
    }

    public static List<PluginSource> getAll() {
        TypedQuery<PluginSource> query = getEm().createNamedQuery("pluginSource.getAll", PluginSource.class);
        return query.getResultList();
    }

    public static List<PluginSource> getBySource(String source) {
        TypedQuery<PluginSource> query = getEm().createNamedQuery("pluginSource.getBySource",
                PluginSource.class).setParameter("source", source);
        return query.getResultList();
    }
}
