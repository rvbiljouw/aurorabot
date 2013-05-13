package ms.aurora.core.model;

import com.avaje.ebean.Ebean;

import javax.persistence.*;
import java.util.List;

/**
 * A generic property entry allowing storage of simple settings in the database.
 * @author Cov
 */
@Entity
public class Property extends AbstractModel {

    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String value;

    public Property() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public static List<Property> getAll() {
        return Ebean.find(Property.class).findList();
    }

    public static Property getByName(String value) {
        return Ebean.find(Property.class).where().eq("name", value).findUnique();
    }


}
