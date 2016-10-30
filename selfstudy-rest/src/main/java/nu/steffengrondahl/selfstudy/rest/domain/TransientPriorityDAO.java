package nu.steffengrondahl.selfstudy.rest.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Steffen on 30-10-2016.
 */
public class TransientPriorityDAO implements GenericDAO<PriorityDTO> {

    public PriorityDTO read(Integer key) {
        PriorityDTO priorityDTO = new PriorityDTO();
        priorityDTO.setId(key);
        priorityDTO.setName(getName(key));
        return priorityDTO;
    }

    public List<PriorityDTO> readAll() {
        List<PriorityDTO> list = new ArrayList<PriorityDTO>();
        for(int i=1; i<6; i++) {
            PriorityDTO priorityDTO = new PriorityDTO();
            priorityDTO.setId(i);
            priorityDTO.setName(getName(i));
            list.add(priorityDTO);
        }
        return list;
    }

    public List<PriorityDTO> readSelected(Integer minPriority, Integer maxPriority, Integer minStatus, Integer maxStatus) {
        return readAll();
    }

    public void create(PriorityDTO priorityDTO) {
        // Not implemented
    }

    public void update(PriorityDTO priorityDTO) {
        // Not implemented
    }

    public void delete(PriorityDTO priorityDTO) {
        // Not implemented
    }

    private String getName(int id) {
        switch (id) {
            case 1:
                return "Very low";
            case 2:
                return "Low";
            case 3:
                return "Medium";
            case 4:
                return "High";
            default:
                return "Very High";
        }
    }
}
