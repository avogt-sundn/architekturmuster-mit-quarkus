package organisationen.attribute;

import java.net.URI;
import java.util.List;

import io.opentelemetry.api.metrics.LongCounter;
import io.opentelemetry.api.metrics.Meter;
import io.quarkus.logging.Log;
import io.smallrye.common.annotation.RunOnVirtualThread;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.json.JsonObject;
import jakarta.json.bind.Jsonb;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 *
 */
@Path("resourcewithmetrics")
@ApplicationScoped
public class ResourceWithMetrics {

    Jsonb jsonb;
    private final LongCounter counter;

    public ResourceWithMetrics(Jsonb jsonb, Meter meter) {

        this.jsonb = jsonb;
        counter = meter.counterBuilder("my-metrics")
                .setDescription("my-metrics")
                .setUnit("invocations")
                .build();
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
        counter.add(1);
        AttributeEntity byId = AttributeEntity.findById(id);
        return Response.ok(jsonb.fromJson(byId.jsonString, JsonObject.class)).build();
    }

    @GET
    @Path("virtual")
    @RunOnVirtualThread
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllVirtual() {
        counter.add(1);
        List<AttributeEntity> list = AttributeEntity.findAll().list();
        return Response.ok(list).build();
    }

    @GET
    @Path("classic")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllClassic() {
        counter.add(1);
        List<AttributeEntity> list = AttributeEntity.findAll().list();
        return Response.ok(list).build();
    }

}
