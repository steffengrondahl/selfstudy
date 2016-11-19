package nu.steffengrondahl.selfstudy.rest;

import nu.steffengrondahl.selfstudy.persist.HyperlinkEntityDAO;
import nu.steffengrondahl.selfstudy.persist.QuerySpecificationFactory;
import nu.steffengrondahl.selfstudy.persist.domain.HyperlinkEntity;
import nu.steffengrondahl.selfstudy.persist.domain.ProjectEntity;
import nu.steffengrondahl.selfstudy.rest.model.HyperlinkDTO;
import nu.steffengrondahl.selfstudy.rest.model.ProjectLightDTO;

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
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Steffen on 18-11-2016.
 */
@Path("/hyperlinks")
public class HyperlinkResource {

    @Context
    private UriInfo uriInfo;

    public HyperlinkResource() {

    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<HyperlinkDTO> readList() {
        List<HyperlinkDTO> list = new ArrayList<HyperlinkDTO>();
        HyperlinkEntityDAO dao = new HyperlinkEntityDAO();
        List<HyperlinkEntity> resultList = dao.query(QuerySpecificationFactory.queryAll());
        for(HyperlinkEntity h : resultList) {
            HyperlinkDTO hyperlinkDTO = new HyperlinkDTO();
            hyperlinkDTO.setId(h.getId());
            hyperlinkDTO.setUrl(h.getUrl());

            ProjectLightDTO projectLightDTO = new ProjectLightDTO();
            projectLightDTO.setId(h.getProject().getId());
            projectLightDTO.setDescription(h.getProject().getDescription());
            hyperlinkDTO.setProject(projectLightDTO);

            list.add(hyperlinkDTO);
        }
        return list;
    }

    @GET
    @Path("{id: \\d+}")
    @Produces(MediaType.APPLICATION_JSON)
    public HyperlinkDTO read(@PathParam("id") int id) {
        HyperlinkDTO hyperlinkDTO = new HyperlinkDTO();
        HyperlinkEntityDAO dao = new HyperlinkEntityDAO();
        HyperlinkEntity hyperlinkEntity = dao.find(id, false);
        if(hyperlinkEntity == null) {
            throw new NotFoundException();
        }

        hyperlinkDTO.setId(hyperlinkEntity.getId());
        hyperlinkDTO.setUrl(hyperlinkEntity.getUrl());

        ProjectLightDTO projectLightDTO = new ProjectLightDTO();
        projectLightDTO.setId(hyperlinkEntity.getProject().getId());
        projectLightDTO.setDescription(hyperlinkEntity.getProject().getDescription());
        hyperlinkDTO.setProject(projectLightDTO);

        return hyperlinkDTO;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response create(HyperlinkDTO hyperlinkDTO) {
        HyperlinkEntity hyperlinkEntity = new HyperlinkEntity();
        hyperlinkEntity.setUrl(hyperlinkDTO.getUrl());

        ProjectEntity projectEntity = new ProjectEntity();
        projectEntity.setId(hyperlinkDTO.getProject().getId());

        hyperlinkEntity.setProject(projectEntity);

        HyperlinkEntityDAO dao = new HyperlinkEntityDAO();
        Integer id = dao.add(hyperlinkEntity);
        String hyperlinkId = Integer.toString(id);
        URI uri = uriInfo.getAbsolutePathBuilder().path(hyperlinkId).build();
        return Response.created(uri).build();
    }

    @DELETE
    @Path("{id: \\d+}")
    public void delete(@PathParam("id") int id) {
        HyperlinkEntity hyperlinkEntity = new HyperlinkEntity();
        hyperlinkEntity.setId(id);
        HyperlinkEntityDAO dao = new HyperlinkEntityDAO();
        dao.delete(hyperlinkEntity);
        // No need to return a Response object, as returning nothing (void)
        // implies returning HTTP 204 No Content
    }

    @PUT
    @Path("{id: \\d+}")
    @Consumes(MediaType.APPLICATION_JSON)
    public Response update(@PathParam("id") int id, HyperlinkDTO hyperlinkDTO) {
        if (hyperlinkDTO.getId() != id) {
            throw new BadRequestException("No support for changing hyperlink id");
        }
        HyperlinkEntity hyperlinkEntity = new HyperlinkEntity();
        hyperlinkEntity.setId(id);
        hyperlinkEntity.setUrl(hyperlinkDTO.getUrl());

        ProjectEntity projectEntity = new ProjectEntity();
        projectEntity.setId(hyperlinkDTO.getProject().getId());
        hyperlinkEntity.setProject(projectEntity);

        HyperlinkEntityDAO dao = new HyperlinkEntityDAO();
        Integer hyperlinkId = dao.update(hyperlinkEntity);

        if(hyperlinkId == id) { // update
            return Response.noContent().build();
        }
        else { // create
            URI uri = uriInfo.getAbsolutePathBuilder().path(Integer.toString(hyperlinkId)).build();
            return Response.created(uri).build();
        }
    }



}
