package nu.steffengrondahl.selfstudy.rest.domain;

/**
 * Created by Steffen on 30-10-2016.
 */
public class ProjectLightDTO {

    private Integer id;

    private String description;

    private PriorityDTO priorityDTO;

    private StatusDTO statusDTO;

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

    public PriorityDTO getPriorityDTO() {
        return priorityDTO;
    }

    public void setPriorityDTO(PriorityDTO priorityDTO) {
        this.priorityDTO = priorityDTO;
    }

    public StatusDTO getStatusDTO() {
        return statusDTO;
    }

    public void setStatusDTO(StatusDTO statusDTO) {
        this.statusDTO = statusDTO;
    }
}
