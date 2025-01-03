package quarkitecture.booking.requestbooking;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import org.junit.jupiter.api.Test;

import io.restassured.http.ContentType;

public class BookingRequestResourceTest {
    @Test
    void testCreateRequest() {
   given().body(""
               )
                .header("Content-Type", "application/json")
                .when().post("/hello")
                .then()
                .statusCode(201)
                .contentType(ContentType.JSON)
                .body("name", equalTo(""));
    }
}
