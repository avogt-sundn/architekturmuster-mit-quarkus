package json_payload_ohne_bean_binding.field;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static uk.co.datumedge.hamcrest.json.SameJSONAs.*;

import java.util.UUID;
import java.util.regex.Pattern;

import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;

@QuarkusTest
@TestHTTPEndpoint(FieldResource.class)
class FieldResourceTest {

    final Pattern locationHeaderMatchingPattern = Pattern.compile("http://.+/.+/(\\d+)");

    @BeforeAll
    static void setup() {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    @Test
    void _SaveObjectFromJson() {
        final String randomUUID = UUID.randomUUID().toString();
        final String payload = """
                    {
                        "vorname": "armin",
                        "nachname": "vogt",
                        "freifeld": {
                            "uuid": "%s",
                            "zusatz": "keine weiteren Informationen"
                        }
                    }
                """.formatted(randomUUID);

        final String location = given().with().contentType(ContentType.JSON).body(
                payload).post().then().log().all().statusCode(equalTo(HttpStatus.SC_CREATED))
                .header("Location", matchesPattern(locationHeaderMatchingPattern)).and().extract().header("Location");

        Long id = getId(location);

        String expected = payload;
        given().with().pathParam("id", id).when().get("{id}").then()
                .statusCode(equalTo(HttpStatus.SC_OK))
                .and().contentType(ContentType.JSON)
                .and().body(not(containsString("\\")))
                .and().body("freifeld.uuid", equalTo(randomUUID))
                .body(sameJSONAs(expected));

    }

    private Long getId(final String location) {

        java.util.regex.Matcher locationFromResponse = locationHeaderMatchingPattern.matcher(location);
        assertTrue(locationFromResponse.matches());
        String group = locationFromResponse.group(1);
        Long id = Long.valueOf(group);
        return id;
    }
}
