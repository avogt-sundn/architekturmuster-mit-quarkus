package organisationen.attribute;

import static io.restassured.RestAssured.*;
import static org.assertj.core.api.Assertions.*;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.http.HttpStatus;
import org.jboss.logging.MDC;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;

/**
 *
 */
@QuarkusTest
@TestHTTPEndpoint(JsonResource.class)
@ExtendWith(TestBase.class)
class JsonResourceTest {

    final Pattern locationHeaderMatchingPattern = Pattern.compile("http://.+/(\\d+)");

    @BeforeAll
    static void setup() {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    @Test
    void understandRegex() {

        Matcher matcher = locationHeaderMatchingPattern.matcher("http://localhost:1234/grumpy/77");
        assertTrue(matcher.matches());
        assertThat(matcher.groupCount()).isEqualTo(1);
        Long id = Long.valueOf(matcher.group(1));
        assertThat(id).withFailMessage("location hatte keine id am Ende, sondern: ", id).isEqualTo(77);
    }

    @Test
    void greedyMatchDoesNotRequireUrlPathPrefix() {

        Matcher matcher = locationHeaderMatchingPattern.matcher("http://localhost:8081/77");
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

        MDC.put("location", location);
        Matcher matcher = locationHeaderMatchingPattern.matcher(location);
        assertTrue(matcher.matches());
        String group = matcher.group(1);
        MDC.put("group", group);

        Long id = Long.valueOf(group);
        given().with().contentType(ContentType.JSON).pathParam("id", id).when().get("{id}").then()
                .statusCode(equalTo(HttpStatus.SC_OK)).and().body("uuid", equalTo(randomUUID));
    }
}
