package organisationen.bearbeiten;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import java.net.URI;

import org.apache.http.HttpStatus;
import org.assertj.core.api.Assertions;
import org.json.JSONException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;

import io.quarkus.test.common.http.TestHTTPResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.swagger.parser.OpenAPIParser;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.parser.core.models.SwaggerParseResult;
import jakarta.inject.Inject;
import jakarta.json.bind.Jsonb;
import organisationen.suchen.modell.Organisation;

@QuarkusTest
class OrganisationenBearbeitenResourceTest {

    private static final int UNDEFINED_ORGANISATION_ID = 0;
    @TestHTTPResource(value = "/organizations")
    URI uri;
    @Inject
    EntityFactory factory;

    @Inject
    Jsonb jsonb;

    @BeforeAll
    static void setup() {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    @Test
    void openapiCheck() {
        SwaggerParseResult result = new OpenAPIParser().readLocation("src/main/resources/openapi/openapi.yaml", null,
                null);
        OpenAPI openAPI = result.getOpenAPI();
        Assertions.assertThat(openAPI).isNotNull();

        openAPI.getPaths().
    }

    @Test
    void _CreateArbeitsversion() {

        Organisation organisation = factory.persistASingleInTx("CreateArbeitsversion", 1);
        final String body = jsonb.toJson(organisation);
        given().with().contentType(ContentType.JSON)
                .body(body).when().post("organizations/" + organisation.getId() + "/draft")
                .then().statusCode(equalTo(HttpStatus.SC_OK)).log().all();

        given().when().get("organizations/" + organisation.getId() + "/draft")
                .then().statusCode(equalTo(HttpStatus.SC_OK)).log().all();
        factory.deleteById(organisation.getId());
    }

    @Test
    void _ValidateBodyFails() throws JSONException {

        final String json = """
                {
                        "status": 400,
                        "title": "Constraint Violation",
                        "violations": [
                            {
                                "field": "createArbeitsversion.organisation.beschreibung",
                                "message": "must not be blank"
                            }
                        ]
                }
                """;

        final String extractableResponse = given().with().contentType(ContentType.JSON)
                .body("""
                                {
                                    "name": "Arnsberg"
                                }
                        """).when().post("organizations/" + UNDEFINED_ORGANISATION_ID + "/draft")
                .then().statusCode(equalTo(HttpStatus.SC_BAD_REQUEST))
                .and().extract().asPrettyString();
        JSONAssert.assertEquals(json, extractableResponse, JSONCompareMode.STRICT);
    }
}
