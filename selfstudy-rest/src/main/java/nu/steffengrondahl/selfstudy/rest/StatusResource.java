package nu.steffengrondahl.selfstudy.rest;

import nu.steffengrondahl.selfstudy.persist.QuerySpecificationFactory;
import nu.steffengrondahl.selfstudy.persist.StatusEntityDAO;
import nu.steffengrondahl.selfstudy.persist.domain.ProjectEntity;
import nu.steffengrondahl.selfstudy.persist.domain.StatusEntity;
import nu.steffengrondahl.selfstudy.rest.model.PriorityDTO;
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
@Path("/statuses")
public class StatusResource {

    public StatusResource() {

    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<StatusDTO> readList() {
        List<StatusDTO> list = new ArrayList<StatusDTO>();
        StatusEntityDAO dao = new StatusEntityDAO();
        List<StatusEntity> resultList = dao.query(QuerySpecificationFactory.queryAll());
        for(StatusEntity se : resultList) {
            StatusDTO statusDTO = new StatusDTO();
            statusDTO.setId(se.getId());
            statusDTO.setName(se.getName());
            list.add(statusDTO);
        }
        return list; //statusDAO.readAll();
    }

    @GET
    @Path("{id: \\d+}")
    @Produces(MediaType.APPLICATION_JSON)
    public StatusDTO read(@PathParam("id") int id) {
        StatusDTO statusDTO = new StatusDTO();
        StatusEntityDAO dao = new StatusEntityDAO();
        StatusEntity statusEntity = dao.find(id, true);
        if(statusEntity == null) {
            throw new NotFoundException();
        }

        statusDTO.setId(statusEntity.getId());
        statusDTO.setName(statusEntity.getName());

        List<ProjectLightDTO> projects = new ArrayList<>();
        for(ProjectEntity pe : statusEntity.getProjects()) {
            ProjectLightDTO projectLightDTO = new ProjectLightDTO();
            projectLightDTO.setId(pe.getId());
            projectLightDTO.setDescription(pe.getDescription());

            PriorityDTO priorityDTO = new PriorityDTO();
            priorityDTO.setId(pe.getPriority().getId());
            priorityDTO.setName(pe.getPriority().getName());
            projectLightDTO.setPriority(priorityDTO);

            projects.add(projectLightDTO);
        }
        statusDTO.setProjects(projects);

        return statusDTO;
    }

}
