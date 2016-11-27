package nu.steffengrondahl.selfstudy.rest;

import nu.steffengrondahl.selfstudy.persist.EstimateEntityDAO;
import nu.steffengrondahl.selfstudy.persist.QuerySpecificationFactory;
import nu.steffengrondahl.selfstudy.persist.domain.EstimateEntity;
import nu.steffengrondahl.selfstudy.persist.domain.ProjectEntity;
import nu.steffengrondahl.selfstudy.rest.model.EstimateDTO;
import nu.steffengrondahl.selfstudy.rest.model.PriorityDTO;
import nu.steffengrondahl.selfstudy.rest.model.ProjectLightDTO;
import nu.steffengrondahl.selfstudy.rest.model.StatusDTO;

import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Steffen on 29-10-2016.
 */
@Path("/estimates")
public class EstimateResource {


    public EstimateResource() {

    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<EstimateDTO> readList() {
        List<EstimateDTO> list = new ArrayList<EstimateDTO>();
        EstimateEntityDAO dao = new EstimateEntityDAO();
        List<EstimateEntity> resultList = dao.query(QuerySpecificationFactory.queryAll());
        for(EstimateEntity e : resultList) {
            EstimateDTO estimateDTO = new EstimateDTO();
            estimateDTO.setId(e.getId());
            estimateDTO.setName(e.getName());
            list.add(estimateDTO);
        }
        return list;
    }

    @GET
    @Path("{id: \\d+}")
    @Produces(MediaType.APPLICATION_JSON)
    public EstimateDTO read(@PathParam("id") int id) {
        EstimateDTO estimateDTO = new EstimateDTO();
        EstimateEntityDAO dao = new EstimateEntityDAO();
        EstimateEntity estimateEntity = dao.find(id, true);
        if(estimateEntity == null) {
            throw new NotFoundException();
        }

        estimateDTO.setId(estimateEntity.getId());
        estimateDTO.setName(estimateEntity.getName());

        List<ProjectLightDTO> projects = new ArrayList<>();
        for(ProjectEntity pe : estimateEntity.getProjects()) {
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

            projects.add(projectLightDTO);
        }
        estimateDTO.setProjects(projects);

        return estimateDTO;
    }

    @OPTIONS
    public Response getSupportedOperations() {
        return Response.noContent().header("Allow", "GET").build();
    }
}
