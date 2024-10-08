package quarkitecture;

import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.*;
import static org.hamcrest.CoreMatchers.*;

@QuarkusTest
@TestHTTPEndpoint(GreetingResource.class)
class GreetingResourceTest {

    @Test
    void testHelloEndpoint() {
        given().with().accept(ContentType.TEXT)
                .when().get()
                .then().statusCode(equalTo(HttpStatus.SC_OK))
                .body(is("Hello from RESTEasy Reactive"));
    }

}
