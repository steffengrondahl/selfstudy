package nu.steffengrondahl.selfstudy.rest;

import nu.steffengrondahl.selfstudy.rest.model.DAOFactory;
import nu.steffengrondahl.selfstudy.rest.model.GenericDAO;
import nu.steffengrondahl.selfstudy.rest.model.PriorityDTO;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * Created by Steffen on 30-10-2016.
 */
@Path("/priorities")
public class PriorityResource {

    private GenericDAO<PriorityDTO> priorityDAO;

    public PriorityResource() {
        priorityDAO = DAOFactory.getPriorityDAO();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<PriorityDTO> readList() {
        return priorityDAO.readAll();
    }

    @GET
    @Path("{id: \\d+}")
    @Produces(MediaType.APPLICATION_JSON)
    public PriorityDTO read(@PathParam("id") int id) {
        return priorityDAO.read(id);
    }

}
