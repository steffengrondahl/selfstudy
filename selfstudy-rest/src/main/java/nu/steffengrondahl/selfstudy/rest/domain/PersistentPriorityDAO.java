package nu.steffengrondahl.selfstudy.rest.domain;

import java.util.List;

/**
 * Created by Steffen on 30-10-2016.
 */
public class PersistentPriorityDAO implements GenericDAO<PriorityDTO> {

    public PriorityDTO read(Integer key) {
        return null;
    }

    public List<PriorityDTO> readAll() {
        return null;
    }

    public List<PriorityDTO> readSelected(Integer minPriority, Integer maxPriority, Integer minStatus, Integer maxStatus) {
        return null;
    }

    public void create(PriorityDTO priorityDTO) {

    }

    public void update(PriorityDTO priorityDTO) {

    }

    public void delete(PriorityDTO priorityDTO) {

    }
}
