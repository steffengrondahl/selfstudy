package nu.steffengrondahl.selfstudy.rest.domain;

import java.util.List;

/**
 * Created by Steffen on 30-10-2016.
 */
public class PersistentStatusDAO implements GenericDAO<StatusDTO> {

    public StatusDTO read(Integer key) {
        return null;
    }

    public List<StatusDTO> readAll() {
        return null;
    }

    public List<StatusDTO> readSelected(Integer minPriority, Integer maxPriority, Integer minStatus, Integer maxStatus) {
        return null;
    }

    public void create(StatusDTO statusDTO) {

    }

    public void update(StatusDTO statusDTO) {

    }

    public void delete(StatusDTO statusDTO) {

    }
}
