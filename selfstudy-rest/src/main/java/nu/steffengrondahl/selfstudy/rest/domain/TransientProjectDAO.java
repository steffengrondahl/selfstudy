package nu.steffengrondahl.selfstudy.rest.domain;

import java.util.List;

/**
 * Created by Steffen on 30-10-2016.
 */
public class TransientProjectDAO implements GenericDAO<ProjectDTO> {

    public ProjectDTO read(Integer key) {
        return null;
    }

    public List<ProjectDTO> readAll() {
        return null;
    }

    public List<ProjectDTO> readSelected(Integer minPriority, Integer maxPriority, Integer minStatus, Integer maxStatus) {
        return null;
    }

    public void create(ProjectDTO projectDTO) {

    }

    public void update(ProjectDTO projectDTO) {

    }

    public void delete(ProjectDTO projectDTO) {

    }
}
