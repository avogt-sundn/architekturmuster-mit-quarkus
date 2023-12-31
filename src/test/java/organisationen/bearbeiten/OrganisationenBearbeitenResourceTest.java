package organisationen.bearbeiten;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import java.net.URI;

import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import io.quarkus.test.common.http.TestHTTPResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;

@QuarkusTest
public class OrganisationenBearbeitenResourceTest {

    @TestHTTPResource(value = "/organizations")
    URI uri;
    @Inject
    EntityFactory factory;

    @BeforeAll
    static void setup() {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    @Test
    void _CreateArbeitsversion() {

        Long id = factory.persistASingleInTx("CreateArbeitsversion", 1);

        given().with().contentType(ContentType.JSON)
                .body("""
                                {
                                    "name": "Arnsberg"
                                }
                        """).when().post("organizations/" + id + "/draft")
                .then().statusCode(equalTo(HttpStatus.SC_OK)).log().all();

        given().when().get("organizations/" + id + "/draft")
                .then().statusCode(equalTo(HttpStatus.SC_OK)).log().all();
        // factory.deleteById(id);
    }
}
