package ms.aurora.core.model;

import com.avaje.ebean.Ebean;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.List;

/**
 * @author tobiewarburton
 */
@Entity
public class Source extends AbstractModel {

    @Id
    @GeneratedValue
    private Long id;
    private String source;
    private boolean devMode;

    public Source(String source, boolean devMode) {
        this.source = source;
        this.devMode = devMode;
    }

    public Source() {

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

    public static List<Source> getAll() {
        return Ebean.find(Source.class).findList();
    }

    public static List<Source> getBySource(String source) {
        return Ebean.find(Source.class).where().eq("source", source).findList();
    }
}

