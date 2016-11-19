package nu.steffengrondahl.selfstudy.persist;

/**
 * Specification of query used for restricting the query
 * Don't instantiate this class direct, used factory nu.steffengrondahl.selfstudy.persist.QuerySpecificationFactory
 *
 *
 * Created by Steffen on 29-10-2016.
 */
public class QuerySpecification {

    private Integer minPriority = 0;
    private Integer maxPriority = 0;
    private Integer minStatus = 0;
    private Integer maxStatus = 0;
    private Integer projectId = null;

    public QuerySpecification() {

    }

    public void setMinPriority(Integer minPriority) {
        this.minPriority = minPriority;
    }

    public Integer getMinPriority() {
        return minPriority;
    }

    public void setMaxPriority(Integer maxPriority) {
        this.maxPriority = maxPriority;
    }

    public Integer getMaxPriority() {
        return maxPriority;
    }

    public void setMinStatus(Integer minStatus) {
        this.minStatus = minStatus;
    }

    public Integer getMinStatus() {
        return minStatus;
    }

    public void setMaxStatus(Integer maxStatus) {
        this.maxStatus = maxStatus;
    }

    public Integer getMaxStatus() {
        return maxStatus;
    }

    public Integer getProjectId() {
        return projectId;
    }

    public void setProjectId(Integer projectId) {
        this.projectId = projectId;
    }
}
