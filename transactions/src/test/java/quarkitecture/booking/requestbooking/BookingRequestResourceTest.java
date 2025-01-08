package quarkitecture.booking.requestbooking;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;

@QuarkusTest
class BookingRequestResourceTest {
    @Test
    void postHello() {

        given().body(
                """
                {
                        "name": "john"
                }
                """)
                .header("Content-Type", "application/json")
                .when().post("/hello")
                .then()
                .statusCode(201)
                .contentType(ContentType.JSON)
                .body("name", equalTo("john"));
    }
}
