package quarkitecture.booking.ordering;

import static io.restassured.RestAssured.given;
import static java.util.concurrent.TimeUnit.*;
import static org.assertj.core.api.Assertions.*;
import static org.awaitility.Awaitility.*;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.quarkus.logging.Log;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.sse.SseEventSource;
import quarkitecture.booking.domain.Hotel;
import quarkitecture.booking.domain.Reservation;
import quarkitecture.booking.domain.TestConstants;

@QuarkusTest
@TestHTTPEndpoint(OrderResource.class)
class OrderResourceTest {

    @Inject
    private ObjectMapper objectMapper;

    // @TestHTTPResource
    URI serverSentEvents_GET_URL;

    @Test
    void newOrder() throws URISyntaxException {
        serverSentEvents_GET_URL = new URI("http://localhost:8080/booking-requests");

        given().when().accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .body(new File(TestConstants.ORDER_JSON_FILE))
                .post()
                .then().statusCode(201);

        // prepare for reading sse
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(serverSentEvents_GET_URL);
        List<Reservation> received = new CopyOnWriteArrayList<>();
        List<Throwable> exc = new CopyOnWriteArrayList<>();

        SseEventSource source = SseEventSource.target(target).build();
        source.register(inboundSseEvent -> {
            try {
                Log.fatal("inbound!!...");
                Reservation value = objectMapper.readValue(inboundSseEvent.readData(), Reservation.class);
                assertThat(value).isInstanceOf(Hotel.class);
                Log.fatal(" --> " + value);

                received
                        .add(value);

            } catch (JsonProcessingException e) {
                exc.add(e);
            }
        });
        source.open();

        await().atMost(2500, MILLISECONDS).until(
                () -> received.size() > 0);

    }

}
