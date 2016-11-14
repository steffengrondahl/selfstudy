package nu.steffengrondahl.selfstudy.persist;

import nu.steffengrondahl.selfstudy.persist.domain.HyperlinkEntity;
import nu.steffengrondahl.selfstudy.persist.domain.ProjectEntity;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.ParameterExpression;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Steffen on 13-11-2016.
 */
public class HyperlinkDAO implements GenericEntityDAO<HyperlinkEntity> {

    @Override
    public Integer add(HyperlinkEntity hyperlinkEntity) {
        EntityManager entityManager = PersistUtil.getEntityManagerFactory().createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.persist(hyperlinkEntity);
        Integer id = hyperlinkEntity.getId();
        entityManager.getTransaction().commit();
        entityManager.close();
        return id;
    }

    @Override
    public void update(HyperlinkEntity hyperlinkEntity) {
        EntityManager entityManager = PersistUtil.getEntityManagerFactory().createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.merge(hyperlinkEntity);
        entityManager.getTransaction().commit();
        entityManager.close();
    }

    @Override
    public void delete(HyperlinkEntity hyperlinkEntity) {
        EntityManager entityManager = PersistUtil.getEntityManagerFactory().createEntityManager();
        entityManager.getTransaction().begin();
        // hyperlink might be detached, so first fetch reference to the attached
        // version of the same project
        HyperlinkEntity attachedHyperlink = entityManager.find(HyperlinkEntity.class, hyperlinkEntity.getId());
        if (attachedHyperlink != null) {
            entityManager.remove(attachedHyperlink);
        }
        entityManager.getTransaction().commit();
        entityManager.close();

    }

    @Override
    public HyperlinkEntity find(Integer key, boolean decorate) {
        EntityManager entityManager = PersistUtil.getEntityManagerFactory().createEntityManager();
        HyperlinkEntity hyperlink = entityManager.find(HyperlinkEntity.class, key);
        entityManager.close();
        return hyperlink;
    }

    @Override
    public List<HyperlinkEntity> query(QuerySpecification specification) {
        /*
        EntityManager entityManager = PersistUtil.getEntityManagerFactory().createEntityManager();
        // fetch data
        TypedQuery<HyperlinkEntity> query = entityManager.createQuery("SELECT h from HyperlinkEntity AS h ORDER BY h.id",
                HyperlinkEntity.class);
        List<HyperlinkEntity> resultList = query.getResultList();
        // close - now the items are detached
        entityManager.close();
        return resultList;
        */
        EntityManager entityManager = PersistUtil.getEntityManagerFactory().createEntityManager();
        entityManager.getTransaction().begin();

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        // Setting type to ProjectEntity, because this is the expected return
        // type (if String, then use String etc)
        CriteriaQuery<HyperlinkEntity> criteriaQuery = criteriaBuilder.createQuery(HyperlinkEntity.class);
        // The object to fetch from
        Root<HyperlinkEntity> hyperlink = criteriaQuery.from(HyperlinkEntity.class);
        criteriaQuery.select(hyperlink);

        // Create predicates
        final List<Predicate> predicates = new ArrayList<Predicate>();
        ParameterExpression<Integer> projectId = criteriaBuilder.parameter(Integer.class);

        // Join with priority table
        Join<HyperlinkEntity, ProjectEntity> project = hyperlink.join("project");
        // Telling the project id is an Integer
        Path<Integer> pathProject = project.get("id");
        // Making predicates
        Predicate conditionProject = criteriaBuilder.equal(pathProject, projectId);
        predicates.add(conditionProject);

        // Applying predicates to where clause
        criteriaQuery.where(criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()])));

        // Finally getting the typed query
        TypedQuery<HyperlinkEntity> q = entityManager.createQuery(criteriaQuery);
        q.setParameter(projectId, specification.getProjectId());
        List<HyperlinkEntity> resultList = q.getResultList();
        entityManager.close();
        return resultList;
    }
}
