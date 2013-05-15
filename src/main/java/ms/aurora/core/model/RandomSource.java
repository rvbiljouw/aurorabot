package ms.aurora.core.model;

import com.avaje.ebean.Ebean;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.List;

/**
 * A database entry for directories from which randoms may be loaded.
 * @author Rick
 */
@Entity
public class RandomSource extends AbstractModel {

    @Id
    @GeneratedValue
    private Long id;
    private String source;
    private boolean devMode;

    public RandomSource(String source, boolean devMode) {
        this.source = source;
        this.devMode = devMode;
    }

    public RandomSource() {

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

    public static List<RandomSource> getAll() {
        return Ebean.find(RandomSource.class).findList();
    }

    public static List<RandomSource> getBySource(String source) {
        return Ebean.find(RandomSource.class).where().eq("source", source).findList();
    }
}
