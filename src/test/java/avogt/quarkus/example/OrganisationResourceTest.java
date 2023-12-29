package avogt.quarkus.example;

import static io.restassured.RestAssured.*;
import static org.assertj.core.api.Assertions.*;
import static org.hamcrest.Matchers.*;

import java.net.URI;

import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import io.quarkus.narayana.jta.QuarkusTransaction;
import io.quarkus.test.common.http.TestHTTPResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import jakarta.transaction.Transactional;
import jakarta.transaction.Transactional.TxType;

@QuarkusTest
@Transactional
class OrganisationResourceTest {
    @TestHTTPResource(value = "/organizations")
    URI uri;

    @BeforeAll
    static void setup() {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    /**
     * @see OrganisationResource#getSingle(Long)
     */
    @Test
    @Transactional(TxType.NOT_SUPPORTED)
    void _GetSingle() {
        QuarkusTransaction.begin();

        // erzeuge einen Datensatz in der Datenbank
        Organisation organisation = Organisation.builder().beschreibung("Stadtkrankenhaus in Berlin").name("Charité")
                .build();
        Adresse adresse = Adresse.builder().strasse("Charitéplatz 1").postleitzahl(10117).stadt("Berlin").build();
        organisation.addAdresse(adresse);
        organisation.persist();
        assertThat(organisation.id).isNotNull();

        QuarkusTransaction.commit();

        // finde diesen Datensatz per REST:
        given().pathParam("id", organisation.id)
                .when().get("organizations/{id}")
                .then().statusCode(equalTo(HttpStatus.SC_OK))
                .and().body("id", equalTo(organisation.id.intValue()));

    }
}
