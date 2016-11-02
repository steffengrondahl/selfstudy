package nu.steffengrondahl.selfstudy.rest.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Steffen on 30-10-2016.
 */
public class TransientStatusDAO implements GenericDAO<StatusDTO> {

    public StatusDTO read(Integer key) {
        StatusDTO statusDTO = new StatusDTO();
        statusDTO.setId(key);
        statusDTO.setName(getName(key));
        return statusDTO;
    }

    public List<StatusDTO> readAll() {
        List<StatusDTO> list = new ArrayList<StatusDTO>();
        for(int i=1; i<4; i++) {
            StatusDTO statusDTO = new StatusDTO();
            statusDTO.setId(i);
            statusDTO.setName(getName(i));
            list.add(statusDTO);
        }
        return list;
    }

    public List<StatusDTO> readSelected(Integer minPriority, Integer maxPriority, Integer minStatus, Integer maxStatus) {
        return readAll();
    }

    public void create(StatusDTO statusDTO) {
        // Not implemented
    }

    public void update(StatusDTO statusDTO) {
        // Not implemented
    }

    public void delete(StatusDTO statusDTO) {
        // Not implemented
    }

    private String getName(int id) {
        switch (id) {
            case 1:
                return "Open";
            case 2:
                return "Started";
            default:
                return "Completed";
        }
    }
}
