package quarkitecture.registration;

import static io.restassured.RestAssured.*;
import static org.assertj.core.api.Assertions.*;
import static org.hamcrest.Matchers.*;

import java.util.UUID;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import quarkitecture.registration.logentry.LogEntry;

/**
 * transactions
 */
@QuarkusTest
class RegisterNewCustomersResourceTest {

    @Test
    void testPostHelloEndpoint() {

        String meinName = "Q-" + UUID.randomUUID().toString().split("-")[1];
        given().body(
                """
                             { "name": "%s" }
                        """.formatted(meinName))
                .header("Content-Type", "application/json")
                .when().post("/hello")
                .then()
                .statusCode(201)
                .contentType(ContentType.JSON)
                .body("name", equalTo(meinName));

        assertThat(LogEntry.find("message like ?1", "%" + meinName).count()).isEqualTo(1L);

        // denselben Namen erneut erfassen produziert einen costraint violation
        given().body(
                """
                        { "name": "%s" }
                        """.formatted(meinName))
                .header("Content-Type", "application/json")
                .when().post("/hello")
                .then()
                .statusCode(409) // 409 Conflict
                .log().all().body("name", equalTo(meinName));

        assertThat(LogEntry.find("message like ?1", "%" + meinName).count())
                .isEqualTo(1L)
                .withFailMessage("weil Log und Greeting table in einer XA Transaktion geschrieben werden," +
                        " darf beim Fehler kein Log vorhanden sein!");

    }
}