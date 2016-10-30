package nu.steffengrondahl.selfstudy.rest.domain;

import java.util.List;

/**
 * Created by Steffen on 30-10-2016.
 */
public class PersistentProjectLightDAO implements GenericDAO<ProjectLightDTO> {

    public ProjectLightDTO read(Integer key) {
        return null;
    }

    public List<ProjectLightDTO> readAll() {
        return null;
    }

    public List<ProjectLightDTO> readSelected(Integer minPriority, Integer maxPriority, Integer minStatus, Integer maxStatus) {
        return null;
    }

    public void create(ProjectLightDTO projectLightDTO) {

    }

    public void update(ProjectLightDTO projectLightDTO) {

    }

    public void delete(ProjectLightDTO projectLightDTO) {

    }
}
