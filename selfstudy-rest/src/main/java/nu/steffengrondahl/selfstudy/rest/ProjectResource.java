package nu.steffengrondahl.selfstudy.rest;

import nu.steffengrondahl.selfstudy.persist.ProjectEntityDAO;
import nu.steffengrondahl.selfstudy.persist.QuerySpecificationFactory;
import nu.steffengrondahl.selfstudy.persist.domain.EstimateEntity;
import nu.steffengrondahl.selfstudy.persist.domain.PriorityEntity;
import nu.steffengrondahl.selfstudy.persist.domain.ProjectEntity;
import nu.steffengrondahl.selfstudy.persist.domain.StatusEntity;
import nu.steffengrondahl.selfstudy.rest.model.EstimateDTO;
import nu.steffengrondahl.selfstudy.rest.model.PriorityDTO;
import nu.steffengrondahl.selfstudy.rest.model.ProjectDTO;
import nu.steffengrondahl.selfstudy.rest.model.ProjectLightDTO;
import nu.steffengrondahl.selfstudy.rest.model.StatusDTO;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.net.URI;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Steffen on 30-10-2016.
 */
@Path("/projects")
public class ProjectResource {

    //private GenericDAO<ProjectDTO> projectDAO;
    //private GenericDAO<ProjectLightDTO> projectLightDAO;

    @Context
    private UriInfo uriInfo;

    public ProjectResource() {
        //projectDAO = DAOFactory.getProjectDAO();
        //projectLightDAO = DAOFactory.getProjectLightDAO();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<ProjectLightDTO> readList() {
        List<ProjectLightDTO> list = new ArrayList<ProjectLightDTO>();

        ProjectEntityDAO dao = new ProjectEntityDAO();
        List<ProjectEntity> resultList = dao.query(QuerySpecificationFactory.queryAll());
        for (ProjectEntity pe : resultList) {
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

        return list; //projectLightDAO.readAll();
    }

    @GET
    @Path("{id: \\d+}")
    @Produces(MediaType.APPLICATION_JSON)
    public ProjectDTO read(@PathParam("id") int id) {

        ProjectDTO projectDTO = new ProjectDTO();
        ProjectEntityDAO dao = new ProjectEntityDAO();
        ProjectEntity projectEntity = dao.find(id, true);
        if (projectEntity == null)
            throw new NotFoundException();

        projectDTO.setId(projectEntity.getId());
        projectDTO.setDescription(projectEntity.getDescription());
        projectDTO.setGoals(projectEntity.getGoals());
        projectDTO.setActions(projectEntity.getActions());
        LocalDate start = projectEntity.getStart();
        if (start != null) {
            projectDTO.setStart(DateTimeFormatter.ISO_LOCAL_DATE.format(start));
        } else {
            projectDTO.setStart("");
        }
        LocalDate deadline = projectEntity.getDeadline();
        if (deadline != null) {
            projectDTO.setDeadline(DateTimeFormatter.ISO_LOCAL_DATE.format(deadline));
        } else {
            projectDTO.setDeadline("");
        }

        EstimateDTO estimateDTO = new EstimateDTO();
        estimateDTO.setId(projectEntity.getEstimate().getId());
        estimateDTO.setName(projectEntity.getEstimate().getName());
        projectDTO.setEstimate(estimateDTO);

        PriorityDTO priorityDTO = new PriorityDTO();
        priorityDTO.setId(projectEntity.getPriority().getId());
        priorityDTO.setName(projectEntity.getPriority().getName());
        projectDTO.setPriority(priorityDTO);

        StatusDTO statusDTO = new StatusDTO();
        statusDTO.setId(projectEntity.getStatus().getId());
        statusDTO.setName(projectEntity.getStatus().getName());
        projectDTO.setStatus(statusDTO);

        for (ProjectEntity pe : projectEntity.getPresupposed()) {
            ProjectLightDTO projectLightDTO = new ProjectLightDTO();
            projectLightDTO.setId(pe.getId());
            projectLightDTO.setDescription(pe.getDescription());

            priorityDTO = new PriorityDTO();
            priorityDTO.setId(pe.getPriority().getId());
            priorityDTO.setName(pe.getPriority().getName());
            projectLightDTO.setPriority(priorityDTO);

            statusDTO = new StatusDTO();
            statusDTO.setId(pe.getStatus().getId());
            statusDTO.setName(pe.getStatus().getName());
            projectLightDTO.setStatus(statusDTO);

            projectDTO.getPresupposed().add(projectLightDTO);
        }

        for (ProjectEntity pe : projectEntity.getSubsequent()) {
            ProjectLightDTO projectLightDTO = new ProjectLightDTO();
            projectLightDTO.setId(pe.getId());
            projectLightDTO.setDescription(pe.getDescription());

            priorityDTO = new PriorityDTO();
            priorityDTO.setId(pe.getPriority().getId());
            priorityDTO.setName(pe.getPriority().getName());
            projectLightDTO.setPriority(priorityDTO);

            statusDTO = new StatusDTO();
            statusDTO.setId(pe.getStatus().getId());
            statusDTO.setName(pe.getStatus().getName());
            projectLightDTO.setStatus(statusDTO);

            projectDTO.getSubsequent().add(projectLightDTO);
        }

        for (ProjectEntity pe : projectEntity.getLinkable()) {
            ProjectLightDTO projectLightDTO = new ProjectLightDTO();
            projectLightDTO.setId(pe.getId());
            projectLightDTO.setDescription(pe.getDescription());

            priorityDTO = new PriorityDTO();
            priorityDTO.setId(pe.getPriority().getId());
            priorityDTO.setName(pe.getPriority().getName());
            projectLightDTO.setPriority(priorityDTO);

            statusDTO = new StatusDTO();
            statusDTO.setId(pe.getStatus().getId());
            statusDTO.setName(pe.getStatus().getName());
            projectLightDTO.setStatus(statusDTO);

            projectDTO.getLinkable().add(projectLightDTO);
        }

        return projectDTO; //projectDAO.read(id);
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(ProjectDTO projectDTO) {
        ProjectEntity projectEntity = new ProjectEntity();
        projectEntity.setDescription(projectDTO.getDescription());
        projectEntity.setGoals(projectDTO.getGoals());
        projectEntity.setActions(projectDTO.getActions());

        LocalDate start = null;
        try {
            start = LocalDate.parse(projectDTO.getStart(), DateTimeFormatter.ISO_LOCAL_DATE);
        } catch (DateTimeParseException e) {
        }
        projectEntity.setStart(start);
        LocalDate deadline = null;
        try {
            deadline = LocalDate.parse(projectDTO.getDeadline(), DateTimeFormatter.ISO_LOCAL_DATE);
        } catch (DateTimeParseException e) {
        }
        projectEntity.setDeadline(deadline);

        EstimateEntity estimateEntity = new EstimateEntity();
        estimateEntity.setId(projectDTO.getEstimate().getId());
        projectEntity.setEstimate(estimateEntity);

        PriorityEntity priorityEntity = new PriorityEntity();
        priorityEntity.setId(projectDTO.getPriority().getId());
        projectEntity.setPriority(priorityEntity);

        StatusEntity statusEntity = new StatusEntity();
        statusEntity.setId(projectDTO.getStatus().getId());
        projectEntity.setStatus(statusEntity);

        ProjectEntityDAO dao = new ProjectEntityDAO();
        Integer id = dao.add(projectEntity);
        String projectId = Integer.toString(id);
        URI uri = uriInfo.getAbsolutePathBuilder().path(projectId).build();
        return Response.created(uri).build();
    }

    @DELETE
    @Path("{id: \\d+}")
    public void delete(@PathParam("id") int id) {
        ProjectEntity projectEntity = new ProjectEntity();
        projectEntity.setId(id);
        ProjectEntityDAO dao = new ProjectEntityDAO();
        dao.delete(projectEntity);
        // No need to return a Response object, as returning nothing (void)
        // implies returning HTTP 204 No Content
    }

    @PUT
    @Path("{id: \\d+}")
    @Consumes(MediaType.APPLICATION_JSON)
    public void update(@PathParam("id") int id, ProjectDTO projectDTO) {
        if (projectDTO.getId() != id) {
            throw new BadRequestException("Requested id does not match id in json data");
        }
        ProjectEntity projectEntity = new ProjectEntity();
        projectEntity.setId(id);
        projectEntity.setDescription(projectDTO.getDescription());
        projectEntity.setGoals(projectDTO.getGoals());
        projectEntity.setActions(projectDTO.getActions());

        LocalDate start = null;
        try {
            start = LocalDate.parse(projectDTO.getStart(), DateTimeFormatter.ISO_LOCAL_DATE);
        } catch (DateTimeParseException e) {
        }
        projectEntity.setStart(start);
        LocalDate deadline = null;
        try {
            deadline = LocalDate.parse(projectDTO.getDeadline(), DateTimeFormatter.ISO_LOCAL_DATE);
        } catch (DateTimeParseException e) {
        }
        projectEntity.setDeadline(deadline);

        EstimateEntity estimateEntity = new EstimateEntity();
        estimateEntity.setId(projectDTO.getEstimate().getId());
        projectEntity.setEstimate(estimateEntity);

        PriorityEntity priorityEntity = new PriorityEntity();
        priorityEntity.setId(projectDTO.getPriority().getId());
        projectEntity.setPriority(priorityEntity);

        StatusEntity statusEntity = new StatusEntity();
        statusEntity.setId(projectDTO.getStatus().getId());
        projectEntity.setStatus(statusEntity);

        // TODO: Fetch presupposed and subsequent


        ProjectEntityDAO dao = new ProjectEntityDAO();
        dao.update(projectEntity);
        // No need to return a Response object, as returning nothing (void)
        // implies returning HTTP 204 No Content
    }
}