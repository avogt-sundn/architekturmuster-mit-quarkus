package quarkitecture.booking.ordering;

import static io.restassured.RestAssured.given;
import static java.util.concurrent.TimeUnit.*;
import static org.awaitility.Awaitility.*;

import java.io.File;
import java.net.URI;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.common.http.TestHTTPResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.sse.SseEventSource;
import quarkitecture.booking.domain.Reservation;
import quarkitecture.booking.domain.TestConstants;

@QuarkusTest
@TestHTTPEndpoint(OrderResource.class)
class OrderResourceTest {

    @Inject
    private ObjectMapper objectMapper;

    @TestHTTPResource
    URI serverSentEvents_GET_URL;

    @Test
    void newOrder() {

        given().when().accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .body(new File(TestConstants.ORDER_JSON_FILE))
                .post()
                .then().statusCode(201);

        // prepare for reading sse
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(serverSentEvents_GET_URL);
        List<Reservation> received = new CopyOnWriteArrayList<>();
        SseEventSource source = SseEventSource.target(target).build();
        source.register(inboundSseEvent -> {
            try {
                received
                        .add(objectMapper.readValue(inboundSseEvent.readData(), Reservation.class));
            } catch (JsonProcessingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        });
        source.open();

        await().atMost(500, MILLISECONDS).until(() -> received.size() > 0);

    }

}
