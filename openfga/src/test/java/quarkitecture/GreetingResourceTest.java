package quarkitecture;

import static io.restassured.RestAssured.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.*;

import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;

import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;

@QuarkusTest
@TestHTTPEndpoint(GreetingResource.class)
@TestMethodOrder(OrderAnnotation.class)
class GreetingResourceTest {
    @Test
    @Order(1)
    public void testListModels() {

        given()
                .when().get("/openfga/authorization-models")
                .then()
                .log().ifValidationFails()
                .statusCode(200)
                .contentType("application/json")
                .body("$", hasSize(1));
    }

    @Test
    @Order(2)
    public void testListTuples() {

        given()
                .when().get("/openfga/authorization-tuples")
                .then()
                .log().ifValidationFails()
                .statusCode(200)
                .contentType("application/json")
                .body("$", hasSize(3),
                        "key.object", containsInAnyOrder("thing:1", "thing:2", "thing:2"),
                        "key.relation", containsInAnyOrder("owner", "owner", "reader"),
                        "key.user", containsInAnyOrder("user:me", "user:you", "user:me"));
    }
    @Test
    @Order(6)
    void Write() {
        given()
                .when().post()
                .then()
                .statusCode(HttpStatus.SC_CREATED)
                .header("location", containsString("/"));
    }

    @Test
    @Order(7)
    void Approve() {
        given()
                .when().get()
                .then()
                .statusCode(HttpStatus.SC_OK)
                .body(is("Hello from RESTEasy Reactive"));
    }

}