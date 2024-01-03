package organisationen.bearbeiten;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import static uk.co.datumedge.hamcrest.json.SameJSONAs.*;

import java.net.URI;

import org.apache.http.HttpStatus;
import org.json.JSONException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import io.quarkus.test.common.http.TestHTTPResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;
import jakarta.json.bind.Jsonb;
import lombok.extern.slf4j.Slf4j;
import organisationen.suchen.modell.Organisation;

@QuarkusTest
@Slf4j
class OrganisationenBearbeitenResourceTest {

    private static final int UNDEFINED_ORGANISATION_ID = 0;
    @TestHTTPResource(value = "/organizations")
    URI uri;
    @Inject
    TestHelperOrganisation factory;

    @Inject
    Jsonb jsonb;

    @BeforeAll
    static void setup() {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    @Test
    void _CreateArbeitsversion() throws JSONException {

        Organisation organisation = factory.persistASingleInTx("CreateArbeitsversion", 1);
        final String body = jsonb.toJson(organisation);
        // 1. create
        given().with().contentType(ContentType.JSON)
                .body(body).when().post("organizations/" + organisation.getId() + "/draft")
                .then().statusCode(equalTo(HttpStatus.SC_OK));
        // 2. read back
        final String result = given().when().get("organizations/" + organisation.getId() + "/draft")
                // .then().statusCode(equalTo(HttpStatus.SC_OK)).and()
                .then().log().all().rootPath("organisation").body("beschreibung", equalTo("CreateArbeitsversion"))
                .and().extract().asPrettyString();

        log.info("result: \n{}", result);

        // JSONAssert.assertEquals(body, result, JSONCompareMode.STRICT);
        factory.deleteById(organisation.getId());
    }

    @Test
    void _ValidateBodyFails() throws JSONException {

        final String expectedErrorResponse = """
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
        given().with().contentType(ContentType.JSON)
                .body("""
                                {
                                    "name": "Arnsberg"
                                }
                        """).when().post("organizations/" + UNDEFINED_ORGANISATION_ID + "/draft")
                .then().statusCode(equalTo(HttpStatus.SC_BAD_REQUEST))
                .and().assertThat().body(sameJSONAs(expectedErrorResponse));
    }
}
