package restassured_specs;

import static io.restassured.RestAssured.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.net.ConnectException;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.specification.RequestSpecification;

@QuarkusTest
@TestHTTPEndpoint(TestableRestResource.class)
class TestableRestResourceTest {

    @Test
    void _BasisTest() {

        given()
                .contentType("application/json")
                .body("{\"name\":\"Hans\"}")
                .post()
                .then().statusCode(201);
    }

    @Test
    void _MitSpecInLocalVariable() {

        RequestSpecification requestSpecification = new RequestSpecBuilder().setContentType("application/json").build();

        given()
                .spec(requestSpecification)
                .log().all()
                .body("{\"name\":\"Hans\"}")
                .post()
                .then().statusCode(201);
    }

    /**
     * Demonstriert, dass ein RequestSpecification in einem Instanzfeld verwendet.
     * Diese muss vor jedem Test neu instanziiert werden. Das kann in der BeforeEach
     * Methode erfolgen.
     */
    RequestSpecification instanceField;

    @BeforeEach
    void setUp() {
        this.instanceField = new RequestSpecBuilder().setContentType("application/json").build();
    }

    @Test
    void _MitSpecInInstanceField() {

        given()
                .spec(this.instanceField)
                .log().all()
                .body("{\"name\":\"Hans\"}")
                .post()
                .then().statusCode(201);
    }

    /**
     * eine einmalige Instanziiereung des RequestSpecification wird immmer Fehler
     * produzieren.
     */
    static RequestSpecification staticField;

    @BeforeAll
    static void setupBeforeAll() {
        staticField = new RequestSpecBuilder().setContentType("application/json").build();
    }

    @Test
    void _MitSpecInStatic() {

        Exception catched = null;
        try {
            given()
                    .spec(staticField)
                    .log().all()
                    .body("{\"name\":\"Statischer Hans\"}")
                    .post()
                    .then().statusCode(201);
        } catch (Exception e) {
            catched = e;
        } finally {
            assertNotNull(catched);
            assertThat(catched)
                    .withFailMessage(
                            """
                                    Die statische Spec wird falsch befüllt, da sie vor dem Quarkus Setup erzeugt wird.
                                    Darum versucht RestAssured sich mit localhost:8080 zu verbinden!
                                    Der Test-Server läuft aber auf 8081.
                                    """)
                    .isInstanceOf(ConnectException.class);
        }
    }
}