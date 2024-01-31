package json_payload_ohne_bean_binding;

import static io.restassured.RestAssured.*;
import static org.assertj.core.api.Assertions.*;
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

    final Pattern locationHeaderMatchingPattern = Pattern.compile("http://.+/(\\d+)");

    @BeforeAll
    static void setup() {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    @Test
    void understandRegex() {

        java.util.regex.Matcher matcher = locationHeaderMatchingPattern.matcher("http://localhost:1234/grumpy/77");
        assertTrue(matcher.matches());
        assertThat(matcher.groupCount()).isEqualTo(1);
        Long id = Long.valueOf(matcher.group(1));
        assertThat(id).withFailMessage("location hatte keine id am Ende, sondern: ", id).isEqualTo(77);
    }

    @Test
    void greedyMatchDoesNotRequireUrlPathPrefix() {

        java.util.regex.Matcher matcher = locationHeaderMatchingPattern.matcher("http://localhost:8081/77");
        assertTrue(matcher.matches());
        assertThat(matcher.groupCount()).isEqualTo(1);
        String group = matcher.group(1);
        Long id = Long.valueOf(group);
        assertThat(id).withFailMessage("location hatte keine id am Ende, sondern: " + id).isEqualTo(77);
    }

    @Test
    void testSaveObjectFromJson() {
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

        given().with().contentType(ContentType.JSON).pathParam("id", id).when().get("{id}").then()
                .statusCode(equalTo(HttpStatus.SC_OK))
                .and().body(not(containsString("\\")))
                .and().body("uuid", equalTo(randomUUID));

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
