package nu.steffengrondahl.selfstudy.rest;

import nu.steffengrondahl.selfstudy.persist.ProjectEntityDAO;
import nu.steffengrondahl.selfstudy.persist.QuerySpecificationFactory;
import nu.steffengrondahl.selfstudy.persist.domain.ProjectEntity;
import nu.steffengrondahl.selfstudy.rest.model.DAOFactory;
import nu.steffengrondahl.selfstudy.rest.model.GenericDAO;
import nu.steffengrondahl.selfstudy.rest.model.PriorityDTO;
import nu.steffengrondahl.selfstudy.rest.model.ProjectDTO;
import nu.steffengrondahl.selfstudy.rest.model.ProjectLightDTO;
import nu.steffengrondahl.selfstudy.rest.model.StatusDTO;

import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
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
        List<ProjectLightDTO> list = new ArrayList<ProjectLightDTO>();

        try {
            ProjectEntityDAO dao = new ProjectEntityDAO();
            List<ProjectEntity> resultList = dao.query(QuerySpecificationFactory.queryAll());
            for(ProjectEntity pe : resultList) {
                ProjectLightDTO projectLightDTO = new ProjectLightDTO();
                projectLightDTO.setId(pe.getId());
                projectLightDTO.setDescription(pe.getDescription());

                PriorityDTO priorityDTO = new PriorityDTO();
                priorityDTO.setId(pe.getPriority().getId());
                priorityDTO.setName(pe.getPriority().getName());
                projectLightDTO.setPriority(priorityDTO);

                StatusDTO statusDTO = new StatusDTO();
                statusDTO.setId(pe.getStatus().getId());
                statusDTO.setName(pe.getStatus().getName());
                projectLightDTO.setStatus(statusDTO);

                list.add(projectLightDTO);
            }
        }
        catch(Exception e) {
            e.printStackTrace();;
        }


        return list; //projectLightDAO.readAll();
    }

    @GET
    @Path("{id: \\d+}")
    @Produces(MediaType.APPLICATION_JSON)
    public ProjectDTO read(@PathParam("id") int id) {

        ProjectDTO projectDTO = new ProjectDTO();
        ProjectEntityDAO dao = new ProjectEntityDAO();
        ProjectEntity projectEntity = dao.find(id, false);
        if(projectEntity == null)
            throw new NotFoundException();

        projectDTO.setId(projectEntity.getId());
        projectDTO.setDescription(projectEntity.getDescription());

        PriorityDTO priorityDTO = new PriorityDTO();
        priorityDTO.setId(projectEntity.getPriority().getId());
        priorityDTO.setName(projectEntity.getPriority().getName());
        projectDTO.setPriority(priorityDTO);

        StatusDTO statusDTO = new StatusDTO();
        statusDTO.setId(projectEntity.getStatus().getId());
        statusDTO.setName(projectEntity.getStatus().getName());
        projectDTO.setStatus(statusDTO);


        return projectDTO; //projectDAO.read(id);
    }


}
