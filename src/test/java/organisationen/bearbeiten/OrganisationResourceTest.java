package organisationen.bearbeiten;

import static io.restassured.RestAssured.*;
import static org.assertj.core.api.Assertions.*;
import static org.hamcrest.Matchers.*;

import java.net.URI;

import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import io.quarkus.test.common.http.TestHTTPResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.RestAssured;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.transaction.Transactional.TxType;
import organisationen.suchen.OrganisationenSuchenResource;
import organisationen.suchen.modell.OrganisationEntity;

@QuarkusTest
class OrganisationResourceTest {
    @TestHTTPResource(value = "/organizations")
    URI uri;

    @Inject
    TestHelperOrganisation factory;

    @BeforeAll
    static void setup() {
        RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
    }

    /**
     * @see OrganisationenSuchenResource#getSingle(Long)
     */
    @Test
    void _GetSingle() {

        Long id = factory.persistASingleInTx("GetSingle", 99).getId();

        // finde diesen Datensatz per REST:
        given().pathParam("id", id)
                .when().get("organizations/{id}")
                .then().statusCode(equalTo(HttpStatus.SC_OK)).log().all()
                .and().body("id", equalTo(id.intValue()))
                .and().body("adressen", hasSize(1))
                .and().body("adressen[0].stadt", equalTo("Berlin"));

        factory.deleteById(id);
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
        factory.persistARangeOfZeroToCountEntitiesInTx(count, "GetPaginated");

        // finde diesen Datensatz per REST:
        given()
                .when().get("organizations?beschreibung=GetPaginated")
                .then().statusCode(equalTo(HttpStatus.SC_OK)).log().all()
                .and().body("id", hasSize(count));

        given()
                .when().get("organizations?beschreibung=GetPaginated&_page=0&_pagesize=2")
                .then().statusCode(equalTo(HttpStatus.SC_OK)).log().all()
                .and().body("id", hasSize(2))
                .and().body("[0].name", equalTo("0"));

        given()
                .when().get("organizations?beschreibung=GetPaginated&_page=1&_pagesize=2")
                .then().statusCode(equalTo(HttpStatus.SC_OK)).log().all()
                .and().body("id", hasSize(2))
                .and().body("[0].name", equalTo("2"));

        cleanUpDatabase();
    }

    @Transactional(TxType.REQUIRES_NEW)
    void cleanUpDatabase() {
        OrganisationEntity.delete("beschreibung", "GetPaginated");
        assertThat(OrganisationEntity.find("beschreibung", "GetPaginated").count()).isZero();
    }

}
