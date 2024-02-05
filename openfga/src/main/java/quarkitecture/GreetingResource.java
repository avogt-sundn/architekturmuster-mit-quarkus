package quarkitecture;

import java.net.URI;
import java.util.List;

import io.quarkiverse.openfga.client.AuthorizationModelClient;
import io.quarkiverse.openfga.client.StoreClient;
import io.quarkiverse.openfga.client.model.AuthorizationModel;
import io.quarkiverse.openfga.client.model.Tuple;
import io.quarkiverse.openfga.client.model.TupleKey;
import io.quarkus.logging.Log;
import io.smallrye.mutiny.Uni;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/hello")
public class GreetingResource {

    AuthorizationModelClient defaultAuthModelClient;

    public GreetingResource(AuthorizationModelClient defaultAuthModelClient, StoreClient storeClient) {
        this.defaultAuthModelClient = defaultAuthModelClient;
        this.storeClient = storeClient;
    }

    @SuppressWarnings("null")
    void approve() {
        if (defaultAuthModelClient.check(
                TupleKey.of("thing:1", "reader", "user:me"), null) != null) {
            Log.info("Allowed!");
        }
    }

    StoreClient storeClient;

    @POST
    public Response write(String body) {

        var authModelClient = storeClient.authorizationModels().defaultModel();
        authModelClient.write(TupleKey.of("thing:1", "reader", "user:me"));
        Log.info("Created!");

        return Response.created(URI.create("/")).build();
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        approve();
        return "Hello from RESTEasy Reactive";
    }

    @GET
    @Path("authorization-models")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<List<AuthorizationModel>> listModels() {
        return storeClient.authorizationModels().listAll();
    }

    @GET
    @Path("authorization-tuples")
    @Produces(MediaType.APPLICATION_JSON)
    public Uni<List<Tuple>> listTuples() {
        return defaultAuthModelClient.readAllTuples();
    }
}
