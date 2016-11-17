package nu.steffengrondahl.selfstudy.persist;

import nu.steffengrondahl.selfstudy.persist.domain.PriorityEntity;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * Created by Steffen on 29-10-2016.
 */
public class PriorityEntityDAO implements GenericEntityDAO<PriorityEntity> {

    public PriorityEntityDAO() {

    }

    public Integer add(PriorityEntity priorityEntity) {
        EntityManager entityManager = PersistUtil.getEntityManagerFactory().createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.persist(priorityEntity);
        Integer id = priorityEntity.getId();
        entityManager.getTransaction().commit();
        entityManager.close();
        return id;
    }

    public Integer update(PriorityEntity priorityEntity) {
        EntityManager entityManager = PersistUtil.getEntityManagerFactory().createEntityManager();
        entityManager.getTransaction().begin();
        PriorityEntity mergedPriorityEntity = entityManager.merge(priorityEntity);
        entityManager.getTransaction().commit();
        entityManager.close();
        return mergedPriorityEntity.getId();
    }

    public void delete(PriorityEntity priorityEntity) {
        EntityManager entityManager = PersistUtil.getEntityManagerFactory().createEntityManager();
        entityManager.getTransaction().begin();
        // priority might be detached, so first fetch reference to the attached
        // version of the same project
        PriorityEntity attachedPriority = entityManager.find(PriorityEntity.class, priorityEntity.getId());
        if (attachedPriority != null) {
            entityManager.remove(attachedPriority);
        }

        entityManager.getTransaction().commit();
        entityManager.close();
    }

    public PriorityEntity find(Integer key, boolean decorate) {
        EntityManager entityManager = PersistUtil.getEntityManagerFactory().createEntityManager();
        entityManager.getTransaction().begin();
        PriorityEntity priority = entityManager.find(PriorityEntity.class, key);
        entityManager.getTransaction().commit();
        entityManager.close();
        return priority;
    }

    public List<PriorityEntity> query(QuerySpecification specification) {
        EntityManager entityManager = PersistUtil.getEntityManagerFactory().createEntityManager();
        entityManager.getTransaction().begin();
        // fetch data
        TypedQuery<PriorityEntity> query = entityManager.createQuery("SELECT p from PriorityEntity AS p ORDER BY p.id",
                PriorityEntity.class);
        List<PriorityEntity> resultList = query.getResultList();
        // close - now the items are detached
        entityManager.getTransaction().commit();
        entityManager.close();
        return resultList;
    }

}
