package nu.steffengrondahl.selfstudy.rest.domain;

import java.util.List;

/**
 * Created by Steffen on 30-10-2016.
 */
public class PersistentEstimateDAO implements GenericDAO<EstimateDTO> {

    public EstimateDTO read(Integer key) {
        return null;
    }

    public List<EstimateDTO> readAll() {
        return null;
    }

    public List<EstimateDTO> readSelected(Integer minPriority, Integer maxPriority, Integer minStatus, Integer maxStatus) {
        return null;
    }

    public void create(EstimateDTO estimateDTO) {

    }

    public void update(EstimateDTO estimateDTO) {

    }

    public void delete(EstimateDTO estimateDTO) {

    }
}
