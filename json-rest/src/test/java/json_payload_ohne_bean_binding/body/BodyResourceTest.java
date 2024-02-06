package json_payload_ohne_bean_binding.body;

import static io.restassured.RestAssured.*;
import static org.assertj.core.api.Assertions.*;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.Matchers.not;
import static org.junit.jupiter.api.Assertions.*;
import static uk.co.datumedge.hamcrest.json.SameJSONAs.*;

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
@TestHTTPEndpoint(BodyResource.class)
class BodyResourceTest {

    final Pattern locationHeaderMatchingPattern = Pattern.compile("http://.+/.+/(\\d+)");

    /*
     * http-Protokoll wird im Fehlerfall ausgedruckt.
     */
    @BeforeAll
    static void setup() {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    @Test
    void _UnderstandRegex() {

        java.util.regex.Matcher matcher = locationHeaderMatchingPattern.matcher("http://localhost:1234/grumpy/77");
        assertTrue(matcher.matches());
        assertThat(matcher.groupCount()).isEqualTo(1);
        Long id = Long.valueOf(matcher.group(1));
        assertThat(id).withFailMessage("location hatte keine id am Ende, sondern: ", id).isEqualTo(77);
    }

    @Test
    void _LocationURIHasResourcePath() {

        java.util.regex.Matcher matcher = locationHeaderMatchingPattern.matcher("http://localhost:8081/77");
        assertFalse(matcher.matches());
    }

    @Test
    void _SaveObjectFromJson() {

        final String randomUUID = UUID.randomUUID().toString();
        final String payload = """
                    {
                        "name": "armin",
                        "surname": "vogt",
                        "uuid": "%s"
                    }
                """.formatted(randomUUID);

        final String location = given().with().contentType(ContentType.JSON).body(
                payload).post().then().log().all().statusCode(equalTo(HttpStatus.SC_CREATED))
                .header("Location", matchesPattern(locationHeaderMatchingPattern)).and().extract().header("Location");

        Long id = getId(location);

        String expected = payload;
        given().with().contentType(ContentType.JSON).pathParam("id", id).when().get("{id}").then()
                .statusCode(equalTo(HttpStatus.SC_OK))
                .and().contentType(ContentType.JSON)
                // escaping mit backslash zeigt falsche Serialisierung an
                .and().body(not(containsString("\\")))
                .and().body("uuid", equalTo(randomUUID))
                .body(sameJSONAs(expected));

    }

    private Long getId(final String location) {

        MDC.put("location header aus der response", location);
        java.util.regex.Matcher locationFromResponse = locationHeaderMatchingPattern.matcher(location);
        assertTrue(locationFromResponse.matches());
        String group = locationFromResponse.group(1);
        MDC.put("id aus dem Suffix der location", group);
        Long id = Long.valueOf(group);
        return id;
    }
}
