package nu.steffengrondahl.selfstudy.rest.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Steffen on 30-10-2016.
 */
public class TransientProjectLightDAO implements GenericDAO<ProjectLightDTO> {

    private Map<Integer, ProjectLightDTO> map;

    public TransientProjectLightDAO() {
        map = new HashMap<Integer, ProjectLightDTO>();
        // fill with dummy data
        TransientPriorityDAO transientPriorityDAO = new TransientPriorityDAO();
        TransientStatusDAO transientStatusDAO = new TransientStatusDAO();
        // first project
        Integer key = 1;
        ProjectLightDTO projectLightDTO = new ProjectLightDTO();
        projectLightDTO.setId(key);
        projectLightDTO.setDescription("First project");
        projectLightDTO.setPriority(transientPriorityDAO.read(3));
        projectLightDTO.setStatus(transientStatusDAO.read(1));
        map.put(key, projectLightDTO);
        // second project
        key = 2;
        projectLightDTO = new ProjectLightDTO();
        projectLightDTO.setId(key);
        projectLightDTO.setDescription("Second project");
        projectLightDTO.setPriority(transientPriorityDAO.read(4));
        projectLightDTO.setStatus(transientStatusDAO.read(2));
        map.put(key, projectLightDTO);
    }


    /**
     * This method should never be called!
     * @param key
     * @return
     */
    public ProjectLightDTO read(Integer key) {
        return map.get(key);
    }

    public List<ProjectLightDTO> readAll() {
        return new ArrayList<ProjectLightDTO>(map.values());
    }

    public List<ProjectLightDTO> readSelected(Integer minPriority, Integer maxPriority, Integer minStatus, Integer maxStatus) {
        List<ProjectLightDTO> list = new ArrayList<ProjectLightDTO>(map.values());
        // TODO: Remove from list
        List<ProjectLightDTO> resultList = new ArrayList<ProjectLightDTO>();
        for(ProjectLightDTO project : list) {
            if(project.getPriority().getId() >= minPriority && project.getPriority().getId() <= maxPriority && project.getStatus().getId() >= minStatus && project.getStatus().getId() <= maxStatus) {
                resultList.add(project);
            }
        }
        return resultList;
    }

    public void create(ProjectLightDTO projectLightDTO) {
        // Not implemented
    }

    public void update(ProjectLightDTO projectLightDTO) {
        // Not implemented
    }

    public void delete(ProjectLightDTO projectLightDTO) {
        // Not implemented (but should perhaps)
    }

}