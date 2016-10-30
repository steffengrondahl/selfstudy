package nu.steffengrondahl.selfstudy.rest.domain;

import java.util.List;

/**
 * Created by Steffen on 30-10-2016.
 */
public class TransientProjectDAO implements GenericDAO<ProjectDTO> {

    public ProjectDTO read(Integer key) {
        // fill with dummy data
        TransientEstimateDAO transientEstimateDAO = new TransientEstimateDAO();
        TransientPriorityDAO transientPriorityDAO = new TransientPriorityDAO();
        TransientStatusDAO transientStatusDAO = new TransientStatusDAO();
        // create dummy project
        ProjectDTO projectDTO = new ProjectDTO();
        projectDTO.setId(key);
        projectDTO.setDescription("First project");
        projectDTO.setEstimateDTO(transientEstimateDAO.read(2));
        projectDTO.setPriorityDTO(transientPriorityDAO.read(3));
        projectDTO.setStatusDTO(transientStatusDAO.read(1));
        return projectDTO;
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
