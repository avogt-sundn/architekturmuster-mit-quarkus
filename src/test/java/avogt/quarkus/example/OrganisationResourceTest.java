package avogt.quarkus.example;

import static io.restassured.RestAssured.*;
import static org.assertj.core.api.Assertions.*;
import static org.hamcrest.Matchers.*;

import java.net.URI;

import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import avogt.quarkus.organisationskatalog.rest.OrganisationResource;
import avogt.quarkus.organisationskatalog.sql.AdresseEntity;
import avogt.quarkus.organisationskatalog.sql.OrganisationEntity;
import io.quarkus.narayana.jta.QuarkusTransaction;
import io.quarkus.test.common.http.TestHTTPResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;

@QuarkusTest
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
    void _GetSingle() {
        QuarkusTransaction.begin();
        // erzeuge einen Datensatz in der Datenbank
        OrganisationEntity organisation = OrganisationEntity.builder().beschreibung("Stadtkrankenhaus in Berlin")
                .name("Charité")
                .build();
        AdresseEntity adresse = AdresseEntity.builder().strasse("Charitéplatz 1").postleitzahl(10117).stadt("Berlin")
                .build();
        organisation.addAdresse(adresse);
        organisation.persist();
        assertThat(organisation.id).isNotNull();

        QuarkusTransaction.commit();
        // finde diesen Datensatz per REST:
        given().pathParam("id", organisation.id)
                .when().get("organizations/{id}")
                .then().statusCode(equalTo(HttpStatus.SC_OK)).log().all()
                .and().body("id", equalTo(organisation.id.intValue()))
                .and().body("adressen", hasSize(1))
                .and().body("adressen[0].stadt", equalTo("Berlin"));

    }
}
