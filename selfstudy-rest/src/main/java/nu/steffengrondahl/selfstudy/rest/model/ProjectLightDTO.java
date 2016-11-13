package nu.steffengrondahl.selfstudy.rest.model;

/**
 * Created by Steffen on 30-10-2016.
 */
public class ProjectLightDTO {

    private Integer id;

    private String description;

    private EstimateDTO estimate;

    private PriorityDTO priority;

    private StatusDTO status;

    public ProjectLightDTO() {

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public EstimateDTO getEstimate() {
        return estimate;
    }

    public void setEstimate(EstimateDTO estimate) {
        this.estimate = estimate;
    }

    public PriorityDTO getPriority() {
        return priority;
    }

    public void setPriority(PriorityDTO priority) {
        this.priority = priority;
    }

    public StatusDTO getStatus() {
        return status;
    }

    public void setStatus(StatusDTO status) {
        this.status = status;
    }
}
