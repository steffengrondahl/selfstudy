package nu.steffengrondahl.selfstudy.rest;

import nu.steffengrondahl.selfstudy.rest.domain.EstimateDTO;
import nu.steffengrondahl.selfstudy.rest.domain.GenericDAO;
import nu.steffengrondahl.selfstudy.rest.domain.TransientEstimateDAO;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * Created by Steffen on 29-10-2016.
 */
@Path("/estimates")
public class EstimateResource {

    private GenericDAO<EstimateDTO> estimateDAO;

    public EstimateResource() {
        // TODO: Change to PersistentEstimateDAO for production
        estimateDAO = new TransientEstimateDAO();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<EstimateDTO> readList() {
        return estimateDAO.readAll();
    }

    @GET
    @Path("{id: \\d+}")
    @Produces(MediaType.APPLICATION_JSON)
    public EstimateDTO read(@PathParam("id") int id) {
        return estimateDAO.read(id);
    }
}
