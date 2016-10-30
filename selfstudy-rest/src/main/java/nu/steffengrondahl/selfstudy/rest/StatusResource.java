package nu.steffengrondahl.selfstudy.rest;

import nu.steffengrondahl.selfstudy.rest.domain.DAOFactory;
import nu.steffengrondahl.selfstudy.rest.domain.GenericDAO;
import nu.steffengrondahl.selfstudy.rest.domain.PriorityDTO;
import nu.steffengrondahl.selfstudy.rest.domain.StatusDTO;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

/**
 * Created by Steffen on 30-10-2016.
 */
@Path("/statuses")
public class StatusResource {

    private GenericDAO<StatusDTO> statusDAO;

    public StatusResource() {
        statusDAO = DAOFactory.getStatusDAO();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<StatusDTO> readList() {
        return statusDAO.readAll();
    }

    @GET
    @Path("{id: \\d+}")
    @Produces(MediaType.APPLICATION_JSON)
    public StatusDTO read(@PathParam("id") int id) {
        return statusDAO.read(id);
    }

}
