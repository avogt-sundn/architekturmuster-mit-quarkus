package avogt.quarkus.organisationskatalog.rest;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import avogt.quarkus.organisationskatalog.OrganisationMapper;
import avogt.quarkus.organisationskatalog.sql.OrganisationEntity;
import io.quarkus.panache.common.Page;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Response;
import lombok.AllArgsConstructor;

@Path("organizations")
@AllArgsConstructor
@ApplicationScoped
public class OrganisationResource {

    OrganisationMapper mapper;

    /**
     * @param id, is a path parameter, like in '/organizations/101'
     * @return the found organization, or null, serialized to '{}'
     * @throws URISyntaxException
     */
    @GET
    @Path("{id}")
    public Response getSingle(@PathParam("id") Long id) throws URISyntaxException {

        return Response.ok(mapper.toDomain(
                OrganisationEntity.findById(id))).location(new URI("/organizations/" + id)).build();
    }

    /**
     * @param id, is a path parameter, like in '/organizations/101'
     * @return the found organization, or null, serialized to '{}'
     */
    @GET
    public Response getPaginated(@QueryParam(value = "_page") int page, int pageSize) {

        List<OrganisationEntity> list = OrganisationEntity.findAll().page(Page.of(page, pageSize)).list();
        return Response.ok(mapper.toDomainList(list)).build();
    }
}
