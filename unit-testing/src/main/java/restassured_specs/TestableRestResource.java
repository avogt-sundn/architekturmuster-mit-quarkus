package restassured_specs;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

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
import jakarta.ws.rs.core.UriInfo;

/**
 *
 */
@Path(TestableRestResource.JSON_PAYLOAD_OHNE_BEAN_BINDING_RESOURCE)
@ApplicationScoped
public class TestableRestResource {

    static final String JSON_PAYLOAD_OHNE_BEAN_BINDING_RESOURCE = "restassured-specs/resource";

    Map<Long, String> store = new HashMap<>();
    AtomicLong sequence = new AtomicLong();

    Jsonb jsonb;

    public TestableRestResource(Jsonb jsonb) {
        this.jsonb = jsonb;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response saveObjectFromJson(JsonObject body, UriInfo info) {

        long id = sequence.incrementAndGet();
        store.put(id, body.toString());
        Log.info(info.getBaseUri().getPath());

        return Response.created(URI.create(String.valueOf(id))).build();
    }

    @GET
    @Path("{id}")
    public Response load(@PathParam("id") Long id) {

        String string = store.get(id);
        return Response.ok(jsonb.fromJson(string, JsonObject.class)).build();
    }

}
