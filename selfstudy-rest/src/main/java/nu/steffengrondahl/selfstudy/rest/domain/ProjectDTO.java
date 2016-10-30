package nu.steffengrondahl.selfstudy.rest.domain;

import java.time.LocalDate;
import java.util.List;

/**
 * Created by Steffen on 30-10-2016.
 */
public class ProjectDTO {

    private Integer id;

    private String description;

    private String goals;

    private String actions;

    private LocalDate start;

    private LocalDate deadline;

    private EstimateDTO estimateDTO;

    private PriorityDTO priorityDTO;

    private StatusDTO statusDTO;

    private List<ProjectLightDTO> presupposed;

    private List<ProjectLightDTO> linkable;

    public ProjectDTO() {

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

    public String getGoals() {
        return goals;
    }

    public void setGoals(String goals) {
        this.goals = goals;
    }

    public String getActions() {
        return actions;
    }

    public void setActions(String actions) {
        this.actions = actions;
    }

    public LocalDate getStart() {
        return start;
    }

    public void setStart(LocalDate start) {
        this.start = start;
    }

    public LocalDate getDeadline() {
        return deadline;
    }

    public void setDeadline(LocalDate deadline) {
        this.deadline = deadline;
    }

    public EstimateDTO getEstimateDTO() {
        return estimateDTO;
    }

    public void setEstimateDTO(EstimateDTO estimateDTO) {
        this.estimateDTO = estimateDTO;
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

    public List<ProjectLightDTO> getPresupposed() {
        return presupposed;
    }

    public void setPresupposed(List<ProjectLightDTO> presupposed) {
        this.presupposed = presupposed;
    }

    public List<ProjectLightDTO> getLinkable() {
        return linkable;
    }

    public void setLinkable(List<ProjectLightDTO> linkable) {
        this.linkable = linkable;
    }
}
