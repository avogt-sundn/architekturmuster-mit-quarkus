package json_payload_ohne_bean_binding;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import org.jboss.resteasy.reactive.RestQuery;

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
@Path("json-payload-ohne-bean-binding")
@ApplicationScoped
public class JsonResource {

    Map<Long, String> store = new HashMap<>();
    AtomicLong sequence = new AtomicLong();

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Transactional
    public Response saveObjectFromJson(JsonObject body) {

        long id = sequence.incrementAndGet();
        store.put(id, body.toString());
        return Response.created(URI.create(""+ id)).build();
    }

    public JsonResource(Jsonb jsonb) {
        this.jsonb = jsonb;
    }

    Jsonb jsonb;

    @GET
    @Path("{id}")
    public Response load(@PathParam("id") Long id) {

        String string = store.get(id);
        return Response.ok(
                jsonb.fromJson(string, JsonObject.class)).build();
    }

    @GET
    public Response search(@RestQuery String[] params) {
        return Response.ok().build();
    }

}
