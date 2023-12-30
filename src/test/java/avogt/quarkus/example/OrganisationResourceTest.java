package avogt.quarkus.example;

import static io.restassured.RestAssured.*;
import static org.assertj.core.api.Assertions.*;
import static org.hamcrest.Matchers.*;

import java.net.URI;
import java.util.stream.IntStream;

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

        Long id;
        QuarkusTransaction.begin();
        {
            OrganisationEntity organisation = create();
            organisation.persist();
            assertThat(organisation.id).isNotNull();
            id = organisation.id;
        }
        QuarkusTransaction.commit();

        // finde diesen Datensatz per REST:
        given().pathParam("id", id)
                .when().get("organizations/{id}")
                .then().statusCode(equalTo(HttpStatus.SC_OK)).log().all()
                .and().body("id", equalTo(id.intValue()))
                .and().body("adressen", hasSize(1))
                .and().body("adressen[0].stadt", equalTo("Berlin"));
        QuarkusTransaction.begin();
        {
            OrganisationEntity.deleteById(id);
        }
        QuarkusTransaction.commit();
    }

    private OrganisationEntity create() {
        // erzeuge einen Datensatz in der Datenbank
        OrganisationEntity organisation = OrganisationEntity.builder().beschreibung("Stadtkrankenhaus in Berlin")
                .name("Charité")
                .build();
        AdresseEntity adresse = AdresseEntity.builder().strasse("Charitéplatz 1").postleitzahl(10117)
                .stadt("Berlin")
                .build();
        organisation.addAdresse(adresse);
        return organisation;
    }

    @Test
    void _GetAll() {
        // finde diesen Datensatz per REST:
        given()
                .when().get("organizations")
                .then().statusCode(equalTo(HttpStatus.SC_OK)).log().all();

    }

    @Test
    void _GetPaginated() {
        final int count = 10;
        IntStream.range(0, count)
                .map(i -> {
                    QuarkusTransaction.begin();
                    OrganisationEntity o = create();
                    o.name = "" + i;
                    o.beschreibung = "GetPaginated";
                    o.persist();
                    System.out.println(i);
                    QuarkusTransaction.commit();

                    return i;
                }).sum();

        // finde diesen Datensatz per REST:
        given()
                .when().get("organizations")
                .then().statusCode(equalTo(HttpStatus.SC_OK)).log().all()
                .and().body("id", hasSize(count));

        given()
                .when().get("organizations?_page=0&_pagesize=2")
                .then().statusCode(equalTo(HttpStatus.SC_OK)).log().all()
                .and().body("id", hasSize(2))
                .and().body("[0].name", equalTo("0"));

        given()
                .when().get("organizations?_page=1&_pagesize=2")
                .then().statusCode(equalTo(HttpStatus.SC_OK)).log().all()
                .and().body("id", hasSize(2))
                .and().body("[0].name", equalTo("2"));
    }

}
