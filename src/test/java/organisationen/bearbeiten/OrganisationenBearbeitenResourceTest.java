package organisationen.bearbeiten;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static uk.co.datumedge.hamcrest.json.SameJSONAs.*;

import java.util.UUID;

import org.apache.http.HttpStatus;
import org.json.JSONException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import io.restassured.config.ObjectMapperConfig;
import io.restassured.http.ContentType;
import io.restassured.mapper.ObjectMapperType;
import jakarta.inject.Inject;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbException;
import lombok.extern.slf4j.Slf4j;
import organisationen.suchen.modell.Organisation;

@QuarkusTest
@Slf4j
@TestHTTPEndpoint(OrganisationenBearbeitenResource.class)
@TestMethodOrder(value = MethodOrderer.OrderAnnotation.class)
@TestInstance(value = TestInstance.Lifecycle.PER_CLASS)
class OrganisationenBearbeitenResourceTest {

    private static final String UNDEFINED_ORGANISATION_ID = UUID.randomUUID().toString();

    @Inject
    TestHelperOrganisation factory;

    @Inject
    Jsonb jsonb;

    final static boolean useJackson = false;

    @BeforeAll
    static void setup() {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
        if (useJackson) {
            RestAssured.config
                    .objectMapperConfig(
                            new ObjectMapperConfig().jackson2ObjectMapperFactory((type, s) -> new ObjectMapper()
                                    .registerModule(new Jdk8Module())
                                    .registerModule(new JavaTimeModule())
                                    .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)));
        } else {
            RestAssured.config().objectMapperConfig(
                    ObjectMapperConfig.objectMapperConfig().defaultObjectMapperType(ObjectMapperType.JSONB));
        }
        ;
    }

    @Test
    void _CreateArbeitsversion() throws JSONException {

        Organisation organisation = factory.persistASingleInTx("CreateArbeitsversion", 1);
        final String body = jsonb.toJson(organisation);
        assertNotNull(organisation.getFachschluessel());

        // 1. create
        given().with().contentType(ContentType.JSON)
                .body(body).when().post(organisation.getFachschluessel() + "/draft")
                .then().statusCode(equalTo(HttpStatus.SC_OK));
        // 2. read back
        final String result = given().when().get(organisation.getFachschluessel() + "/draft")
                .then().statusCode(equalTo(HttpStatus.SC_OK))
                .and()
                .rootPath("organisation")
                .body("beschreibung", equalTo("CreateArbeitsversion"))
                .and().extract().asPrettyString();

        log.info("result: \n{}", result);
        // JSONAssert.assertEquals(body, result, JSONCompareMode.STRICT);
        factory.deleteById(organisation.getId());
    }

    @Test
    void _Update() throws JSONException {

        Organisation organisation = factory.persistASingleInTx("UpdateArbeitsversion", 1);
        assertNotNull(organisation.getFachschluessel());

        // 1. create
        given().with().contentType(ContentType.JSON).body(jsonb.toJson(organisation))
                .when().post(organisation.getFachschluessel() + "/draft")
                .then().statusCode(equalTo(HttpStatus.SC_OK));
        // 2. change
        organisation.setBeschreibung("NeueBeschreibung");
        given().with().contentType(ContentType.JSON).body(jsonb.toJson(organisation))
                .when().put(organisation.getFachschluessel() + "/draft")
                .then().statusCode(equalTo(HttpStatus.SC_OK));
        // 2. read back
        given()
                .when().get(organisation.getFachschluessel() + "/draft")
                .then().rootPath("organisation")
                .body("beschreibung", equalTo("NeueBeschreibung"));

        factory.deleteById(organisation.getId());
    }

    @Test
    void ChangeStatus() throws JsonbException, JSONException {
        Organisation organisation = factory.persistASingleInTx("ChangeStatus", 1);
        assertNotNull(organisation.getFachschluessel());

        // 1. create
        given().with().contentType(ContentType.JSON).body(jsonb.toJson(organisation))
                .when().post(organisation.getFachschluessel() + "/draft")
                .then().statusCode(equalTo(HttpStatus.SC_OK));
        // 2. status change
        given().with().contentType(ContentType.TEXT).body("ZUR_FREIGABE").log().all()
                .when().patch(organisation.getFachschluessel() + "/draft")
                .then().statusCode(equalTo(HttpStatus.SC_OK))
                .and().body(equalTo("ZUR_FREIGABE")).log().all();
        // 3. new status
        Arbeitsversion orgExtracted = given().with().contentType(ContentType.JSON).body(jsonb.toJson(organisation))
                .when().get(organisation.getFachschluessel() + "/draft")
                .then().statusCode(equalTo(HttpStatus.SC_OK))
                .and().rootPath("organisation").extract().as(Arbeitsversion.class);
        log.info("orgExtracted: {}", orgExtracted);
        JSONAssert.assertEquals(jsonb.toJson(organisation), jsonb.toJson(orgExtracted.organisation),
                JSONCompareMode.STRICT);

        factory.deleteById(organisation.getId());
    }

    @Test
    @Disabled("""
            dieses sameJSONAs funktioniert nicht: der rootPath("organisation") ist nicht wirksam
            """)
    void Bug_CreateArbeitsversion() throws JSONException {

        Organisation organisation = factory.persistASingleInTx("CreateArbeitsversion", 1);
        final String body = jsonb.toJson(organisation);
        // 1. create
        given().with().contentType(ContentType.JSON)
                .body(body).when().post(organisation.getId() + "/draft")
                .then().statusCode(equalTo(HttpStatus.SC_OK));
        // 2. read back
        final String result = given().when().get(organisation.getId() + "/draft")
                .then()
                .rootPath("organisation")
                // dieses sameJSONAs funktioniert nicht: der rootPath("organisation") ist nicht
                // wirksam
                .body(sameJSONAs(body))
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
                            "name": "Arnsberg",
                            "fachschluessel": """
                        + "\"" + UUID.randomUUID() + "\"" +
                        """
                                }""")
                .when().post(UNDEFINED_ORGANISATION_ID + "/draft").then().statusCode(equalTo(HttpStatus.SC_BAD_REQUEST))
                .and().body(sameJSONAs(expectedErrorResponse));
    }
}
