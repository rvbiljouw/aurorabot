package ms.aurora.core.model;

import com.avaje.ebean.Ebean;

import javax.persistence.*;
import java.util.List;

/**
 * A database entry for directories from which scripts may be loaded.
 * @author Rick
 */
@Entity
public class ScriptSource extends AbstractModel {

    @Id
    @GeneratedValue
    private Long id;
    private String source;
    private boolean devMode;

    public ScriptSource(String source, boolean devMode) {
        this.source = source;
        this.devMode = devMode;
    }

    public ScriptSource() {

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

    public static List<ScriptSource> getAll() {
        return Ebean.find(ScriptSource.class).findList();
    }

    public static List<ScriptSource> getBySource(String source) {
        return Ebean.find(ScriptSource.class).where().eq("source", source).findList();
    }
}
