package nu.steffengrondahl.selfstudy.persist;

/**
 * Created by Steffen on 29-10-2016.
 */
public class QuerySpecificationFactory {

    public QuerySpecificationFactory() {
    }

    public static QuerySpecification queryAll() {
        return new QuerySpecification();
    }

    public static QuerySpecification queryByPriorityRange(Integer minPriority, Integer maxPriority) {
        QuerySpecification specification = new QuerySpecification();
        specification.setMinPriority(Math.min(minPriority, maxPriority));
        specification.setMinPriority(Math.max(minPriority, maxPriority));
        return specification;
    }

    public static QuerySpecification queryByStatusRange(Integer minStatus, Integer maxStatus) {
        QuerySpecification specification = new QuerySpecification();
        specification.setMinStatus(Math.min(minStatus, maxStatus));
        specification.setMinStatus(Math.max(minStatus, maxStatus));
        return specification;
    }

    public static QuerySpecification queryByPriorityAndStatusRange(Integer minPriority, Integer maxPriority, Integer minStatus, Integer maxStatus) {
        QuerySpecification specification = new QuerySpecification();
        specification.setMinPriority(Math.min(minPriority, maxPriority));
        specification.setMinPriority(Math.max(minPriority, maxPriority));
        specification.setMinStatus(Math.min(minStatus, maxStatus));
        specification.setMinStatus(Math.max(minStatus, maxStatus));
        return specification;
    }

    public static QuerySpecification queryByProjectId(Integer projectId) {
        QuerySpecification specification = new QuerySpecification();
        specification.setProjectId(projectId);
        return specification;

    }
}
