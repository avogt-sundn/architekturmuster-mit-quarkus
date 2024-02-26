package json_payload_ohne_bean_binding.body;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.json.JsonObject;
import jakarta.json.bind.Jsonb;
import jakarta.ws.rs.Consumes;
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
     * als Konstante definiert kann man den Path auch referenzieren (Location
     * Header)
     */
    static final String BASEURI = "json-payload-ohne-bean-bindings/bodies";

    Map<Long, String> store = new HashMap<>();
    AtomicLong sequence = new AtomicLong();

    public BodyResource(Jsonb jsonb) {
        this.jsonb = jsonb;
    }

    Jsonb jsonb;

    @Operation(summary = "erzeugt ein neues Objekt", description = "ein neues Objekt vom Typ wird angelegt und mit einer ID versehen. Die ID wird im Location Header mitgeteilt.")
    @APIResponse(responseCode = "200", description = "error")
    /**
     * String Typ kann json im body empfangen, auch wenn der content-type header
     * nicht vom Client gesetzt wurde.
     * Um das setzen des content-type zwingend zu fordern, nutzen wir diese
     * annotation
     *
     * <pre>
     *  &#64;Consumes(MediaType.APPLICATION_JSON)
     * </pre>
     *
     * @param body
     *            - der http body wird als String übergeben.
     * @return Response - die http Antwort mit Http Status CREATED(201) und Location
     *         Header mit korrekter URL für GET
     */
    @POST
    @Consumes("application/json")
    public Response saveObjectFromJson(@Parameter(description = "json payload") String body) {

        long id = sequence.incrementAndGet();
        store.put(id, body);
        /**
         * Location Header ist vorgeschrieben bei POST. Status 201 Created ist
         * vorgeschrieben bei POST. Location Header muss hier den ganzen Path mitgeteilt
         * bekommen.
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
