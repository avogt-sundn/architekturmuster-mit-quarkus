package standalone;

import static io.restassured.RestAssured.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.jboss.resteasy.reactive.ResponseStatus;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;

@ApplicationScoped
@Path("generisch")
public class RestResourceType extends RRT<Datensatz> {
}

record Datensatz(Long id, UUID fachschluessel, String daten) {
}

class RRT<T> {
    Map<UUID, T> drafts = new HashMap<>();

    @GET
    @Path("{fachschluessel}/draft")
    @ResponseStatus(200)
    public T get(@PathParam("fachschluessel") UUID fachschluessel) {
        return drafts.get(fachschluessel);
    }

    @POST
    @Produces("application/json")
    @Path("{fachschluessel}/draft")
    @ResponseStatus(204)
    public void post(@PathParam("fachschluessel") UUID fachschluessel, T entity) {
        drafts.put(fachschluessel, entity);
    }
}

@QuarkusTest
class RRTTest {

    @Test
    void test() {

        UUID randomUUID = UUID.randomUUID();
        given().body(
                """
                        {  "daten": "meine daten",
                            "uuid": "%s"
                        }
                        """.formatted(randomUUID)).accept("application/json")
                .contentType("application/json")
                .when().post("/generisch/{id}/draft", randomUUID)
                .then().statusCode(204);
        when().get("/generisch/{id}/draft", randomUUID).then().statusCode(200);
    }
}
