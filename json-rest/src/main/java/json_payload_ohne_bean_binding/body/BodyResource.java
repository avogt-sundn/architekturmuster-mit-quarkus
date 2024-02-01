package json_payload_ohne_bean_binding.body;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

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
@Path(BodyResource.BASEURI)
@ApplicationScoped
class BodyResource {

    static final String BASEURI = "json-payload-ohne-bean-binding/body";

    Map<Long, String> store = new HashMap<>();
    AtomicLong sequence = new AtomicLong();

    public BodyResource(Jsonb jsonb) {
        this.jsonb = jsonb;
    }

    Jsonb jsonb;

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response saveObjectFromJson(String body) {

        long id = sequence.incrementAndGet();
        store.put(id, body);
        return Response.created(URI.create(BASEURI + "/" + id))
                .build();
    }

    @GET
    @Path("{id}")
    public Response load(@PathParam("id") Long id) {

        String string = store.get(id);
        return Response.ok(
                jsonb.fromJson(string, JsonObject.class)).build();
    }

}
