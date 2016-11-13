package nu.steffengrondahl.selfstudy.rest.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Steffen on 30-10-2016.
 */
public class ProjectDTO extends ProjectLightDTO {

    private String goals;

    private String actions;

    private String start;

    private String deadline;

    private List<ProjectLightDTO> presupposed = new ArrayList<>();

    private List<ProjectLightDTO> subsequent = new ArrayList<>();

    private List<ProjectLightDTO> linkable = new ArrayList<>();

    public ProjectDTO() {
        super();
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

    public String getStart() {
        return start;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public String getDeadline() {
        return deadline;
    }

    public void setDeadline(String deadline) {
        this.deadline = deadline;
    }

    public List<ProjectLightDTO> getPresupposed() {
        return presupposed;
    }

    public void setPresupposed(List<ProjectLightDTO> presupposed) {
        this.presupposed = presupposed;
    }

    public List<ProjectLightDTO> getSubsequent() {
        return subsequent;
    }

    public void setSubsequent(List<ProjectLightDTO> subsequent) {
        this.subsequent = subsequent;
    }

    public List<ProjectLightDTO> getLinkable() {
        return linkable;
    }

    public void setLinkable(List<ProjectLightDTO> linkable) {
        this.linkable = linkable;
    }
}
