package nu.steffengrondahl.selfstudy.rest;

import nu.steffengrondahl.selfstudy.rest.domain.DAOFactory;
import nu.steffengrondahl.selfstudy.rest.domain.GenericDAO;
import nu.steffengrondahl.selfstudy.rest.domain.ProjectDTO;
import nu.steffengrondahl.selfstudy.rest.domain.ProjectLightDTO;
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
@Path("/projects")
public class ProjectResource {

    private GenericDAO<ProjectDTO> projectDAO;
    private GenericDAO<ProjectLightDTO> projectLightDAO;

    public ProjectResource() {
        projectDAO = DAOFactory.getProjectDAO();
        projectLightDAO = DAOFactory.getProjectLightDAO();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<ProjectLightDTO> readList() {
        return projectLightDAO.readAll();
    }

    @GET
    @Path("{id: \\d+}")
    @Produces(MediaType.APPLICATION_JSON)
    public ProjectDTO read(@PathParam("id") int id) {
        return projectDAO.read(id);
    }


}
