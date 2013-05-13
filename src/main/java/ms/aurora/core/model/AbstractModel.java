package ms.aurora.core.model;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Query;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * @author Rick
 */
public class AbstractModel {

    public final void save() {
        Ebean.save(this);
    }

    public final void update() {
        Ebean.update(this);
    }

    public final void remove() {
        Ebean.delete(this);
    }

    public final void refresh() {
        Ebean.refresh(this);
    }

    public static <T> Query<T> finder(Class<T> t) {
        return Ebean.find(t);
    }

}
