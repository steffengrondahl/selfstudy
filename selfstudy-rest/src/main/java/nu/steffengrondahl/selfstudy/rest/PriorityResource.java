package nu.steffengrondahl.selfstudy.rest;

import nu.steffengrondahl.selfstudy.persist.PriorityEntityDAO;
import nu.steffengrondahl.selfstudy.persist.QuerySpecificationFactory;
import nu.steffengrondahl.selfstudy.persist.domain.PriorityEntity;
import nu.steffengrondahl.selfstudy.rest.model.PriorityDTO;

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
@Path("/priorities")
public class PriorityResource {

    public PriorityResource() {

    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<PriorityDTO> readList() {
        List<PriorityDTO> list = new ArrayList<PriorityDTO>();
        PriorityEntityDAO dao = new PriorityEntityDAO();
        List<PriorityEntity> resultList = dao.query(QuerySpecificationFactory.queryAll());
        for (PriorityEntity p : resultList) {
            PriorityDTO priorityDTO = new PriorityDTO();
            priorityDTO.setId(p.getId());
            priorityDTO.setName(p.getName());
            list.add(priorityDTO);
        }
        return list;
    }

    @GET
    @Path("{id: \\d+}")
    @Produces(MediaType.APPLICATION_JSON)
    public PriorityDTO read(@PathParam("id") int id) {
        PriorityDTO priorityDTO = new PriorityDTO();
        PriorityEntityDAO dao = new PriorityEntityDAO();
        PriorityEntity priorityEntity = dao.find(id, false);
        if (priorityEntity == null) {
            throw new NotFoundException();
        }

        priorityDTO.setId(priorityEntity.getId());
        priorityDTO.setName(priorityEntity.getName());

        return priorityDTO;
    }

}
