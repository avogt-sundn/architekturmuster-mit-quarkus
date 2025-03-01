package json_payload_ohne_bean_binding.field;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import io.quarkus.logging.Log;
import io.smallrye.common.annotation.RunOnVirtualThread;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.validation.Valid;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Response;

/**
 * Der path der Resource wird durch die path Attribute verlängert, nie ersetzt!
 */
@Path(FieldResource.BASEURI)
@ApplicationScoped
class FieldResource {

    static final String BASEURI = "json-payload-ohne-bean-bindings/fields";

    Map<Long, Organisation> store = new HashMap<>();
    AtomicLong sequence = new AtomicLong();

    @POST
    @Consumes("application/json")
    public Response saveObjectFromJson(@Valid Organisation body) {

        long id = sequence.incrementAndGet();
        store.put(id, body);
        return Response.created(URI.create(BASEURI + "/" + id)).build();
    }

    @RunOnVirtualThread
    @GET
    @Path("{id}")
    public Response load(@PathParam("id") Long id) {

        Log.info("GET ist angekommen");
        Organisation org = store.get(id);
        return Response.ok(org).build();
    }

}