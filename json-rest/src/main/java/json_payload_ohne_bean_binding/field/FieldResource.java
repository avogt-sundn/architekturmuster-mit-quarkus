package json_payload_ohne_bean_binding.field;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.json.bind.Jsonb;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Response;

/**
 *
 */
@Path(FieldResource.BASEURI)
@ApplicationScoped
class FieldResource {

    static final String BASEURI = "json-payload-ohne-bean-binding/field";

    Map<Long, Organisation> store = new HashMap<>();
    AtomicLong sequence = new AtomicLong();

    public FieldResource(Jsonb jsonb) {
        this.jsonb = jsonb;
    }

    Jsonb jsonb;

    @POST
    public Response saveObjectFromJson(Organisation body) {

        long id = sequence.incrementAndGet();
        store.put(id, body);
        return Response.created(URI.create(BASEURI + "/" + id)).build();
    }

    @GET
    @Path("{id}")
    public Response load(@PathParam("id") Long id) {

        Organisation org = store.get(id);
        return Response.ok(org).build();
    }

}