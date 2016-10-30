package nu.steffengrondahl.selfstudy.rest.domain;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Steffen on 30-10-2016.
 */
public class TransientEstimateDAO implements GenericDAO<EstimateDTO> {

    public EstimateDTO read(Integer key) {
        EstimateDTO estimateDTO = new EstimateDTO();
        estimateDTO.setId(key);
        estimateDTO.setName(getName(key));
        return estimateDTO;
    }

    public List<EstimateDTO> readAll() {
        List<EstimateDTO> list = new ArrayList<EstimateDTO>();
        for(int i=1; i<5;i++) {
            EstimateDTO estimateDTO = new EstimateDTO();
            estimateDTO.setId(i);
            estimateDTO.setName(getName(i));
            list.add(estimateDTO);
        }
        return list;
    }

    public List<EstimateDTO> readSelected(Integer minPriority, Integer maxPriority, Integer minStatus, Integer maxStatus) {
        return readAll();
    }

    public void create(EstimateDTO estimateDTO) {
        // Not implemented
    }

    public void update(EstimateDTO estimateDTO) {
        // Not implemented
    }

    public void delete(EstimateDTO estimateDTO) {
        // Not implemented
    }

                                        private String getName(int id) {
                                            switch(id) {
                                                case 1:
                return "Hours";
            case 2:
                return "Days";
            case 3:
                return "Weeks";
            default:
                return "Months";
        }
    }
}
