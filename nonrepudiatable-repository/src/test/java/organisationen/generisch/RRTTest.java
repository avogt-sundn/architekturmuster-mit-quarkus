package organisationen.generisch;

import static io.restassured.RestAssured.*;
import static org.assertj.core.api.Assertions.*;
import static org.hamcrest.Matchers.*;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.jboss.resteasy.reactive.ResponseStatus;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.quarkus.logging.Log;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.validation.Valid;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;

@ApplicationScoped
@Path("fachdatum")
class RestResourceType extends RRT<Fachdatum> {

    public RestResourceType() {
        super(Fachdatum.class.getSimpleName());
    }
}

record Fachdatum(Long id, UUID fachschluessel, String daten) {
}

/**
 * generische Implementierung eines REST-Endpunkts für ein Repository vom Typ T
 */
abstract class RRT<T> {
    Map<UUID, T> drafts = new HashMap<>();

    final String pathPrefix;

    protected RRT(String pathPrefix) {
        this.pathPrefix = pathPrefix;
    }

    @GET
    @Path("/{fachschluessel}/draft")
    @ResponseStatus(200)
    public T get(
            @PathParam("fachschluessel") UUID fachschluessel) {

        T t = drafts.get(fachschluessel);
        Log.infof("Lese %s", t);
        return t;
    }

    @POST
    @Produces("application/json")
    @Path("/{fachschluessel}/draft")
    @ResponseStatus(204)
    public void post(@PathParam("typ") String versionskreiswurzel, @PathParam("fachschluessel") UUID fachschluessel,
            @Valid T t) {
        Log.infof("Schreibe %s", t);

        drafts.put(fachschluessel, t);
    }
}

@QuarkusTest
class RRTTest {

    @Test
    void test() {

        UUID randomUUID = UUID.randomUUID();
        // Datensatz schreiben
        given().log().all().body(
                """
                        {   "id": 77,
                            "daten": "meine daten",
                            "fachschluessel": "%s"
                        }
                        """.formatted(randomUUID)).contentType("application/json").accept("application/json")
                .contentType("application/json")
                .when().post("/fachdatum/{id}/draft", randomUUID)
                .then().statusCode(204);
        // denselben Datensatz lesen
        given().log().all().when().accept("application/json").get("/fachdatum/{id}/draft", randomUUID)
                .then().statusCode(200)
                .and().body("fachschluessel", equalTo(randomUUID.toString()));
    }

    @Inject
    ObjectMapper objectMapper;

    @Test
    void jackson() throws JsonProcessingException {

        String uid = "f47ac10b-58cc-4372-a567-0e02b2c3d479";
        String json = objectMapper.writeValueAsString(new Fachdatum(1L, UUID
                .fromString(uid), "meine daten"));
        Fachdatum value = objectMapper.readValue(json, Fachdatum.class);

        assertThat(json)
                .contains("meine daten")
                .contains(uid);
        assertThat(value).extracting("daten").isEqualTo("meine daten");
        assertThat(value).extracting("fachschluessel").isEqualTo(UUID
                .fromString(uid));
    }
}
