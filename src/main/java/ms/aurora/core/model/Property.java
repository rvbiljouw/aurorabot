package ms.aurora.core.model;

import javax.persistence.*;
import java.util.List;

/**
 * User: Cov
 * Date: 11/04/13
 * Time: 21:10
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
