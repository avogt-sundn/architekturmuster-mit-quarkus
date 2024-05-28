package quarkitecture;

import static io.restassured.RestAssured.*;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.equalTo;

import org.apache.http.HttpStatus;
import org.junit.jupiter.api.Test;

import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;

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