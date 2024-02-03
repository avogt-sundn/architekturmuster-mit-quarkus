package organisationen.attribute;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpStatus;
import org.jboss.logging.MDC;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;

/**
 *
 */
@QuarkusTest
@TestHTTPEndpoint(JsonResource.class)
class JsonResourceTest {

    static final Pattern locationHeaderMatchingPattern = Pattern.compile("http://.+/(\\d+)");

    @BeforeAll
    static void setup() {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    @AfterEach
    void After() {
        MDC.clear();
    }

    @Test
    void SaveObjectFromJson() {
        final String randomUUID = UUID.randomUUID().toString();
        final String location = given().with().contentType(ContentType.JSON).body("""
                {
                    "name": "armin",
                    "surname": "vogt",
                    "uuid": "%s"
                }
                """.formatted(randomUUID)).post().then().log().all().statusCode(equalTo(HttpStatus.SC_CREATED))
                .header("Location", matchesPattern(locationHeaderMatchingPattern)).and().extract().header("Location");

        MDC.put("location", location);
        Matcher matcher = locationHeaderMatchingPattern.matcher(location);
        assertTrue(matcher.matches());
        String group = matcher.group(1);
        MDC.put("group", group);

        Long id = Long.valueOf(group);
        given().with().contentType(ContentType.JSON).pathParam("id", id).when().get("{id}").then()
                .statusCode(equalTo(HttpStatus.SC_OK)).and().body("uuid", equalTo(randomUUID));

    }

    @Test
    void SearchJsonObject() {
        given().with()
                .param("surname", "vogt")
                .when().get().then().assertThat()
                .statusCode(equalTo(HttpStatus.SC_OK))
                .body("name", equalTo("armin"));
    }
}
