package avogt.quarkus.example;

import java.net.URI;
import java.net.URISyntaxException;

import io.quarkus.panache.common.Page;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Response;

@Path("organizations")
public class OrganisationResource {

    /**
     * @param id, is a path parameter, like in '/organizations/101'
     * @return the found organization, or null, serialized to '{}'
     * @throws URISyntaxException
     */
    @GET
    @Path("{id}")
    public Response getSingle(@PathParam("id") Long id) throws URISyntaxException {

        return Response.ok(
                Organisation.findById(id)).location(new URI("/organizations/" + id)).build();
    }

    /**
     * @param id, is a path parameter, like in '/organizations/101'
     * @return the found organization, or null, serialized to '{}'
     * @throws URISyntaxException
     */
    @GET
    public Response getPaginated(@QueryParam(value = "_page") int page, int pageSize) {

        return Response.ok(
                Organisation.findAll().page(Page.of(page, pageSize)).list())
                .build();
    }
}
