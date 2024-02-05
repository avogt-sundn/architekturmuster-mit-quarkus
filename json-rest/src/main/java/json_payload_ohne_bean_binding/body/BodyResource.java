package json_payload_ohne_bean_binding.body;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.json.JsonObject;
import jakarta.json.bind.Jsonb;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Response;

/**
 * der path der Resource wird durch die path Attribute verlängert, nie ersetzt!
 */
@Path(BodyResource.BASEURI)
@ApplicationScoped
class BodyResource {

    /**
     * als Konstante definiert kann man den Path auch referenzieren (Location Header)
     */
    static final String BASEURI = "json-payload-ohne-bean-binding/body";

    Map<Long, String> store = new HashMap<>();
    AtomicLong sequence = new AtomicLong();

    public BodyResource(Jsonb jsonb) {
        this.jsonb = jsonb;
    }

    Jsonb jsonb;

    /**
     *
     * @param body
     * @return
     */
    @POST
    public Response saveObjectFromJson(String body) {

        long id = sequence.incrementAndGet();
        store.put(id, body);
        /**
         * Location Header muss hier den ganzen Path mitgeteilt bekommen.
         */
        return Response.created(URI.create(BASEURI + "/" + id)).build();
    }

    @GET
    @Path("{id}")
    public Response load(@PathParam("id") Long id) {

        String string = store.get(id);
        return Response.ok(jsonb.fromJson(string, JsonObject.class)).build();
    }

}
