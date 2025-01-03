package quarkitecture.booking.ordering;

import static io.restassured.RestAssured.*;

import org.junit.jupiter.api.Test;

import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;

@QuarkusTest
@TestHTTPEndpoint(OrderResource.class)
class OrderResourceTest {

    @Test
    void testCreateAnOrder() {
        given().when().accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .post()
                .then().statusCode(201);
    }

}
