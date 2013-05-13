package ms.aurora.core.model;

import javax.persistence.*;
import java.util.List;

/**
 * A generic property entry allowing storage of simple settings in the database.
 * @author Cov
 */
@Entity
@NamedQueries({
        @NamedQuery(name = "property.getByName", query = "select p from Property p where p.name = :name"),
        @NamedQuery(name = "property.getAll", query = "select p from Property p")
})
public final class Property extends AbstractModel {

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
        TypedQuery<Property> query = getEm().createNamedQuery("property.getAll", Property.class);
        return query.getResultList();
    }

    public static List<Property> getByName(String value) {
        TypedQuery<Property> query = getEm().createNamedQuery("property.getByName", Property.class).setParameter("name", value);
        return query.getResultList();
    }


}
