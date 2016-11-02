package nu.steffengrondahl.selfstudy.rest.model;

/**
 * Created by Steffen on 30-10-2016.
 */
public class DAOFactory {
    private static boolean persistent = false;

    public static GenericDAO<EstimateDTO> getEstimateDAO() {
        if(persistent) {
            return new PersistentEstimateDAO();
        }
        else {
            return new TransientEstimateDAO();
        }
    }

    public static GenericDAO<PriorityDTO> getPriorityDAO() {
        if(persistent) {
            return new PersistentPriorityDAO();
        }
        else {
            return new TransientPriorityDAO();
        }

    }

    public static GenericDAO<ProjectDTO> getProjectDAO() {
        if(persistent) {
            return new PersistentProjectDAO();
        }
        else {
            return new TransientProjectDAO();
        }
    }

    public static GenericDAO<ProjectLightDTO> getProjectLightDAO() {
        if(persistent) {
            return new PersistentProjectLightDAO();
        }
        else {
            return new TransientProjectLightDAO();
        }
    }

    public static GenericDAO<StatusDTO> getStatusDAO() {
        if(persistent) {
            return new PersistentStatusDAO();
        }
        else {
            return new TransientStatusDAO();
        }
    }


}
