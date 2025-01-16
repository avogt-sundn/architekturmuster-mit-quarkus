package quarkitecture.booking.ordering;

import static io.restassured.RestAssured.given;
import static java.util.concurrent.TimeUnit.*;
import static org.awaitility.Awaitility.*;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import io.quarkus.logging.Log;
import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.common.http.TestHTTPResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import jakarta.inject.Inject;
import jakarta.ws.rs.client.Client;
import jakarta.ws.rs.client.ClientBuilder;
import jakarta.ws.rs.client.WebTarget;
import jakarta.ws.rs.sse.SseEventSource;
import quarkitecture.booking.domain.Car;
import quarkitecture.booking.domain.Flight;
import quarkitecture.booking.domain.Hotel;
import quarkitecture.booking.domain.Reservation;
import quarkitecture.booking.domain.Tour;

@QuarkusTest
@TestHTTPEndpoint(OrderResource.class)
class OrderResourceTest {
    private static final String FLIGHTID = "RM99100";
    private static final String ORDER_JSON_FILE = "target/test-OrderResourceTest.json";

    @Inject
    private ObjectMapper objectMapper;

    /**
     * base uri without path
     */
    @TestHTTPResource
    URI serverSentEvents_GET_URL;

    private void generateJsonForTesting() throws IOException {

        Order order = new Order();
        Tour tour = new Tour();
        order.tour = tour;
        tour.includedBookings = new HashSet<>();
        final LocalDateTime lift = LocalDateTime.now();
        tour.includedBookings.addAll(
                Set.of(
                        new Flight(FLIGHTID, "PAD", "TLS", lift),
                        new Hotel(), new Car()));

        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        objectMapper.registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());
        objectMapper.writeValue(new File(ORDER_JSON_FILE), order);
    }

    @Test
    void newOrder() throws IOException, InterruptedException {
        generateJsonForTesting();
        serverSentEvents_GET_URL = serverSentEvents_GET_URL.resolve("/booking-requests");

        given().when().accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .body(new File(ORDER_JSON_FILE))
                .post()
                .then().statusCode(201);

        // prepare for reading sse
        Client client = ClientBuilder.newClient();
        WebTarget target = client.target(serverSentEvents_GET_URL);
        List<Reservation> received = new CopyOnWriteArrayList<>();
        List<Throwable> exc = new CopyOnWriteArrayList<>();

        SseEventSource source = SseEventSource.target(target).build();
        Log.info("waiting at uri: " + serverSentEvents_GET_URL);
        source.register(inboundSseEvent -> {
            try {
                Log.info("inbound!!...");
                Reservation value = objectMapper.readValue(inboundSseEvent.readData(), Reservation.class);

                Log.info(" --> " + value);
                received
                        .add(value);

            } catch (JsonProcessingException e) {
                exc.add(e);
            }
        });
        source.open();

        Thread.sleep(1000);
        await().atMost(1000, MILLISECONDS).until(
                () -> received.size() > 0);

    }

}
