package ms.aurora.core.model;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.Query;
import org.apache.log4j.Logger;

import javax.persistence.*;

/**
 * @author Rick
 */
@MappedSuperclass
public class AbstractModel {
    private static final Logger logger = Logger.getLogger(AbstractModel.class);

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

    public static <T> void test(Class<T> clazz) {
        if (clazz.getAnnotation(Entity.class) != null) {
            Ebean.find(clazz).findList();
            logger.info("Tested "+ clazz.getCanonicalName());
        } else {
            logger.info("Didn't test " + clazz.getCanonicalName() + " because it doesn't have an Entity annotation.");
        }
    }

    public static <T> Query<T> finder(Class<T> t) {
        return Ebean.find(t);
    }

}
