package organisationen.attribute;

import java.net.URI;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.json.JsonObject;
import jakarta.json.bind.Jsonb;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 *
 */
@Path("jsonresource")
@ApplicationScoped
public class JsonResource {

    Jsonb jsonb;

    public JsonResource(Jsonb jsonb) {
        this.jsonb = jsonb;
    }

    @POST
    @Transactional
    public Response saveObjectFromJson(JsonObject body) {

        AttributeEntity attributeEntity = new AttributeEntity();
        Log.info(body);
        attributeEntity.jsonString = body.toString();
        attributeEntity.persist();
        Long id = attributeEntity.id;

        return Response.created(URI.create("/jsonresource/" + id)).build();
    }

    @GET
    @Path("{id}")
    public Response load(@PathParam("id") Long id) {

        AttributeEntity byId = PanacheEntityBase.findById(id);
        return Response.ok(jsonb.fromJson(byId.jsonString, JsonObject.class)).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response search(@QueryParam("surname") String surname) {

        AttributeEntity firstByKeyValue = AttributeEntity.findFirstByKeyValue("surname", surname);
        return Response.ok(firstByKeyValue.jsonString).build();
    }

}
