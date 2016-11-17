package nu.steffengrondahl.selfstudy.persist;

import nu.steffengrondahl.selfstudy.persist.domain.EstimateEntity;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * Created by Steffen on 29-10-2016.
 */
public class EstimateEntityDAO implements GenericEntityDAO<EstimateEntity> {

    public EstimateEntityDAO() {

    }

    public Integer add(EstimateEntity estimateEntity) {
        EntityManager entityManager = PersistUtil.getEntityManagerFactory().createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.persist(estimateEntity);
        Integer id = estimateEntity.getId();
        entityManager.getTransaction().commit();
        entityManager.close();
        return id;
    }

    public Integer update(EstimateEntity estimateEntity) {
        EntityManager entityManager = PersistUtil.getEntityManagerFactory().createEntityManager();
        entityManager.getTransaction().begin();
        EstimateEntity mergedEstimateEntity = entityManager.merge(estimateEntity);
        entityManager.getTransaction().commit();
        entityManager.close();
        return mergedEstimateEntity.getId();
    }

    public void delete(EstimateEntity estimateEntity) {
        EntityManager entityManager = PersistUtil.getEntityManagerFactory().createEntityManager();
        entityManager.getTransaction().begin();
        // estimate might be detached, so first fetch reference to the attached
        // version of the same project
        EstimateEntity attachedEstimate = entityManager.find(EstimateEntity.class, estimateEntity.getId());
        if(attachedEstimate != null) {
            entityManager.remove(attachedEstimate);
        }
        entityManager.getTransaction().commit();
        entityManager.close();

    }

    public EstimateEntity find(Integer key, boolean decorate) {
        EntityManager entityManager = PersistUtil.getEntityManagerFactory().createEntityManager();
        entityManager.getTransaction().begin();
        EstimateEntity estimate = entityManager.find(EstimateEntity.class, key);
        entityManager.getTransaction().commit();
        entityManager.close();
        return estimate;
    }

    public List<EstimateEntity> query(QuerySpecification specification) {
        EntityManager entityManager = PersistUtil.getEntityManagerFactory().createEntityManager();
        entityManager.getTransaction().begin();
        // fetch data
        TypedQuery<EstimateEntity> query = entityManager.createQuery("SELECT e from EstimateEntity AS e ORDER BY e.id", EstimateEntity.class);
        List<EstimateEntity> resultList = query.getResultList();
        // close - now the items are detached
        entityManager.getTransaction().commit();
        entityManager.close();
        return resultList;
    }
}
