package nu.steffengrondahl.selfstudy.rest.model;

import java.util.List;

/**
 * Created by Steffen on 30-10-2016.
 */
public class PriorityDTO {

    private Integer id;

    private String name;

    private List<ProjectLightDTO> projects;

    public PriorityDTO() {

    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<ProjectLightDTO> getProjects() {
        return projects;
    }

    public void setProjects(List<ProjectLightDTO> projects) {
        this.projects = projects;
    }
}
