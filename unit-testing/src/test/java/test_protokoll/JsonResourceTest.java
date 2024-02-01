package test_protokoll;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.UUID;
import java.util.regex.Pattern;

import org.apache.http.HttpStatus;
import org.jboss.logging.MDC;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;

@QuarkusTest
@TestHTTPEndpoint(JsonResource.class)
class JsonResourceTest {

    final Pattern locationHeaderMatchingPattern = Pattern.compile("http://.+/(.+)/(\\d+)");

    @BeforeAll
    static void setup() {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    @Test
    void _SaveObjectFromJson() {
        final String randomUUID = UUID.randomUUID().toString();

        final String location = given().with().contentType(ContentType.JSON).body("""
                {
                    "name": "armin",
                    "surname": "vogt",
                    "uuid": "%s"
                }
                """.formatted(randomUUID)).post().then().log().all().statusCode(equalTo(HttpStatus.SC_CREATED))
                .header("Location", matchesPattern(locationHeaderMatchingPattern)).and().extract().header("Location");

        Long id = getId(location);
        MDC.put("id empfangen", id);
        given().with().contentType(ContentType.JSON).pathParam("id", id).when().get("{id}").then()
                .statusCode(equalTo(HttpStatus.SC_OK)).and().body(not(containsString("\\"))).and()
                .body("uuid", equalTo(randomUUID));
    }

    private Long getId(final String location) {

        MDC.put("location header aus der response", location);
        java.util.regex.Matcher locationFromResponse = locationHeaderMatchingPattern.matcher(location);
        assertTrue(locationFromResponse.matches());
        String group = locationFromResponse.group(2);

        MDC.put("id aus dem Suffix der location", group);
        Long id = Long.valueOf(group);
        return id;
    }
}
