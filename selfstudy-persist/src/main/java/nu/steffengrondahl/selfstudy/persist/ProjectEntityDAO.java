package nu.steffengrondahl.selfstudy.persist;

import nu.steffengrondahl.selfstudy.persist.domain.PriorityEntity;
import nu.steffengrondahl.selfstudy.persist.domain.ProjectEntity;
import nu.steffengrondahl.selfstudy.persist.domain.StatusEntity;

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
 * Created by Steffen on 29-10-2016.
 */
public class ProjectEntityDAO implements GenericEntityDAO<ProjectEntity> {

    public ProjectEntityDAO() {

    }

    public Integer add(ProjectEntity projectEntity) {
        EntityManager entityManager = PersistUtil.getEntityManagerFactory().createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.persist(projectEntity);
        Integer id = projectEntity.getId();
        entityManager.getTransaction().commit();
        entityManager.close();
        return id;
    }

    public void update(ProjectEntity projectEntity) {
        EntityManager entityManager = PersistUtil.getEntityManagerFactory().createEntityManager();
        entityManager.getTransaction().begin();
        entityManager.merge(projectEntity);
        entityManager.getTransaction().commit();
        entityManager.close();
    }

    public void delete(ProjectEntity projectEntity) {
        EntityManager entityManager = PersistUtil.getEntityManagerFactory().createEntityManager();
        entityManager.getTransaction().begin();
        // project might be detached, so first fetch reference to the attached
        // version of the same project
        ProjectEntity attachedProject = entityManager.find(ProjectEntity.class, projectEntity.getId());
        if(attachedProject != null) {
            entityManager.remove(attachedProject);
        }

        entityManager.getTransaction().commit();
        entityManager.close();
    }

    public ProjectEntity find(Integer key, boolean decorate) {

        EntityManager entityManager = PersistUtil.getEntityManagerFactory().createEntityManager();
        entityManager.getTransaction().begin();

        // fetch data
        ProjectEntity project = entityManager.find(ProjectEntity.class, key);
        if (decorate && project != null) {
            // Fetch entities explicit is required as the mapping has fetch =
            // FetchType.LAZY

            // Fetch entities explicit is required as the mapping has fetch =
            // FetchType.LAZY

            // Fetch all project entities
            // Notice we can not use ProjectDAO, because it closes the
            // transaction so we will compare detached objects
            TypedQuery<ProjectEntity> queryAll = entityManager
                    .createQuery("SELECT p from ProjectEntity AS p ORDER BY p.id", ProjectEntity.class);
            List<ProjectEntity> allProjects = queryAll.getResultList();

            allProjects.remove(project);
            for (ProjectEntity presupposed : project.getPresupposed()) {
                allProjects.remove(presupposed);
            }
            for (ProjectEntity subsequent : project.getSubsequent()) {
                allProjects.remove(subsequent);
            }

            project.setLinkable(allProjects);
        }
        entityManager.getTransaction().commit();
        entityManager.close();

        return project;
    }

    public List<ProjectEntity> query(QuerySpecification specification) {

        boolean restrictByPriority = (specification.getMaxPriority() > specification.getMinPriority());
        boolean restrictByStatus = (specification.getMaxStatus() > specification.getMinStatus());

        EntityManager entityManager = PersistUtil.getEntityManagerFactory().createEntityManager();
        entityManager.getTransaction().begin();

        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        // Setting type to ProjectEntity, because this is the expected return
        // type (if String, then use String etc)
        CriteriaQuery<ProjectEntity> criteriaQuery = criteriaBuilder.createQuery(ProjectEntity.class);
        // The object to fetch from
        Root<ProjectEntity> project = criteriaQuery.from(ProjectEntity.class);
        criteriaQuery.select(project);

        // Create predicates
        final List<Predicate> predicates = new ArrayList<Predicate>();
        ParameterExpression<Integer> pMinPriority = criteriaBuilder.parameter(Integer.class);
        ParameterExpression<Integer> pMaxPriority = criteriaBuilder.parameter(Integer.class);
        ParameterExpression<Integer> pMinStatus = criteriaBuilder.parameter(Integer.class);
        ParameterExpression<Integer> pMaxStatus = criteriaBuilder.parameter(Integer.class);

        if (restrictByPriority) {
            // Join with priority table
            Join<ProjectEntity, PriorityEntity> priority = project.join("priority");
            // Telling the priority id is an Integer
            Path<Integer> pathPriority = priority.get("id");
            // Making predicates
            Predicate conditionMinPriority = criteriaBuilder.greaterThanOrEqualTo(pathPriority, pMinPriority);
            Predicate conditionMaxPriority = criteriaBuilder.lessThanOrEqualTo(pathPriority, pMaxPriority);
            predicates.add(conditionMinPriority);
            predicates.add(conditionMaxPriority);
        }
        if (restrictByStatus) {
            // Join with status table
            Join<ProjectEntity, StatusEntity> status = project.join("status");
            // Telling the status id is an Integer
            Path<Integer> pathStatus = status.get("id");
            // Making predicates
            Predicate conditionMinStatus = criteriaBuilder.greaterThanOrEqualTo(pathStatus, pMinStatus);
            Predicate conditionMaxStatus = criteriaBuilder.lessThanOrEqualTo(pathStatus, pMaxStatus);
            predicates.add(conditionMinStatus);
            predicates.add(conditionMaxStatus);
        }

        // Applying predicates to where clause
        criteriaQuery.where(criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()])));

        // Ordering
        if (restrictByPriority && restrictByStatus) {
            criteriaQuery.orderBy(criteriaBuilder.desc(project.get("priority")),
                    criteriaBuilder.asc(project.get("status")));
        } else if (restrictByPriority) {
            criteriaQuery.orderBy(criteriaBuilder.desc(project.get("priority")));
        } else if (restrictByStatus) {
            criteriaQuery.orderBy(criteriaBuilder.asc(project.get("status")));
        } else {
            criteriaQuery.orderBy(criteriaBuilder.asc(project.get("id")));
        }

        // Finally getting the typed query
        TypedQuery<ProjectEntity> q = entityManager.createQuery(criteriaQuery);
        if (restrictByPriority) {
            q.setParameter(pMinPriority, specification.getMinPriority());
            q.setParameter(pMaxPriority, specification.getMaxPriority());
        }
        if (restrictByStatus) {
            q.setParameter(pMinStatus, specification.getMinStatus());
            q.setParameter(pMaxStatus, specification.getMaxStatus());
        }
        List<ProjectEntity> resultList = q.getResultList();

        return resultList;
    }

}
