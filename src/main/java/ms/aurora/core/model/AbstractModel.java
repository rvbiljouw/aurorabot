package ms.aurora.core.model;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 * @author rvbiljouw
 */
public class AbstractModel {
    private static final EntityManagerFactory factory =
            Persistence.createEntityManagerFactory("default");


    public final void save() {
        EntityManager em = getEm();
        em.getTransaction().begin();
        em.persist(this);
        em.getTransaction().commit();
    }

    public final void update() {
        EntityManager em = getEm();
        em.getTransaction().begin();
        em.merge(this);
        em.getTransaction().commit();
    }

    public final void remove() {
        EntityManager em = getEm();
        em.getTransaction().begin();
        em.remove(em.merge(this));
        em.getTransaction().commit();
    }

    public final void refresh() {
        EntityManager em = getEm();
        em.refresh(this);
    }

    public static EntityManager getEm() {
        return factory.createEntityManager();
    }

}
