package organisationen.attribute;

import java.net.URI;

import org.jboss.logging.MDC;
import org.jboss.resteasy.reactive.RestQuery;

import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.json.JsonObject;
import jakarta.json.bind.Jsonb;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 *
 */
@Path("jsonresource")
@ApplicationScoped
public class JsonResource {

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response saveObjectFromJson(JsonObject body) {

        AttributeEntity attributeEntity = new AttributeEntity();
        Log.info(body);
        attributeEntity.jsonString = body.toString();
        attributeEntity.persist();
        Long id = attributeEntity.id;

        return Response.created(URI.create("/jsonresource/" + id)).build();
    }

    public JsonResource(Jsonb jsonb) {
        this.jsonb = jsonb;
    }

    Jsonb jsonb;

    @GET
    @Path("{id}")
    public Response load(@PathParam("id") Long id) {

        AttributeEntity byId = AttributeEntity.findById(id);
        MDC.put("load", byId);
        return Response.ok(
            jsonb.fromJson(byId.jsonString,JsonObject.class)
            ).build();
    }

    @GET
    public Response search(@RestQuery String[] params) {

        return Response.ok().build();
    }

}
