package nu.steffengrondahl.selfstudy.rest;

import nu.steffengrondahl.selfstudy.persist.EstimateEntityDAO;
import nu.steffengrondahl.selfstudy.persist.QuerySpecificationFactory;
import nu.steffengrondahl.selfstudy.persist.domain.EstimateEntity;
import nu.steffengrondahl.selfstudy.rest.model.EstimateDTO;

import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
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
        EstimateEntity estimateEntity = dao.find(id, false);
        if(estimateEntity == null) {
            throw new NotFoundException();
        }

        estimateDTO.setId(estimateEntity.getId());
        estimateDTO.setName(estimateEntity.getName());

        return estimateDTO;
    }
}
