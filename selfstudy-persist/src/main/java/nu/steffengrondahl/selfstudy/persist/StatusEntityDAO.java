package nu.steffengrondahl.selfstudy.persist;

import nu.steffengrondahl.selfstudy.persist.domain.StatusEntity;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * Created by Steffen on 29-10-2016.
 */
public class StatusEntityDAO implements GenericEntityDAO<StatusEntity> {

    public StatusEntityDAO() {

    }

    public Integer add(StatusEntity statusEntity) {
        EntityManager entityManager = PersistUtil.getEntityManagerFactory().createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.persist(statusEntity);
        Integer id = statusEntity.getId();
        entityManager.getTransaction().commit();
        entityManager.close();
        return id;
    }

    public Integer update(StatusEntity statusEntity) {
        EntityManager entityManager = PersistUtil.getEntityManagerFactory().createEntityManager();
        entityManager.getTransaction().begin();
        StatusEntity mergedStatusEntity = entityManager.merge(statusEntity);
        entityManager.getTransaction().commit();
        entityManager.close();
        return mergedStatusEntity.getId();
    }

    public void delete(StatusEntity statusEntity) {
        EntityManager entityManager = PersistUtil.getEntityManagerFactory().createEntityManager();
        entityManager.getTransaction().begin();
        // status might be detached, so first fetch reference to the attached
        // version of the same project
        StatusEntity attachedStatus = entityManager.find(StatusEntity.class, statusEntity.getId());
        if(attachedStatus != null) {
            entityManager.remove(attachedStatus);
        }

        entityManager.getTransaction().commit();
        entityManager.close();
    }

    public StatusEntity find(Integer key, boolean decorate) {
        EntityManager entityManager = PersistUtil.getEntityManagerFactory().createEntityManager();
        entityManager.getTransaction().begin();
        StatusEntity status = entityManager.find(StatusEntity.class, key);
        entityManager.getTransaction().commit();
        entityManager.close();
        return status;
    }

    public List<StatusEntity> query(QuerySpecification specification) {
        EntityManager entityManager = PersistUtil.getEntityManagerFactory().createEntityManager();
        entityManager.getTransaction().begin();
        // fetch data
        TypedQuery<StatusEntity> query = entityManager.createQuery("SELECT s from StatusEntity AS s ORDER BY s.id",
                StatusEntity.class);
        List<StatusEntity> resultList = query.getResultList();
        // close - now the items are detached
        entityManager.getTransaction().commit();
        entityManager.close();
        return resultList;
    }

}
