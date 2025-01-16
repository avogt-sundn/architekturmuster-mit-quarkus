package quarkitecture.booking.ordering;

import static io.restassured.RestAssured.given;
import static org.awaitility.Awaitility.*;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

import org.assertj.core.api.Assertions;
import org.eclipse.microprofile.rest.client.RestClientBuilder;
import org.jboss.resteasy.reactive.client.SseEvent;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import io.quarkus.test.common.http.TestHTTPEndpoint;
import io.quarkus.test.common.http.TestHTTPResource;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import io.smallrye.mutiny.Multi;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
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
    URI baseUri;

    UUID underTest;

    private void generateJsonForTesting() throws IOException {

        Order order = new Order();
        Tour tour = new Tour();
        order.tour = tour;
        tour.includedBookings = new HashSet<>();
        final LocalDateTime lift = LocalDateTime.now();
        Flight myFlight = new Flight(FLIGHTID, "PAD", "TLS", lift);
        this.underTest = myFlight.id; // remember the uuid
        tour.includedBookings.addAll(
                Set.of(
                        myFlight,
                        new Hotel(), new Car()));

        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        objectMapper.registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());
        objectMapper.writeValue(new File(ORDER_JSON_FILE), order);
    }

    /**
     * Deklariert die zu testende URL
     */
    interface SseClient {
        @GET
        @Path("/booking-requests")
        @Produces(MediaType.SERVER_SENT_EVENTS)
        Multi<SseEvent<Reservation>> getEvents();
    }

    @Test
    void newOrder() throws IOException, InterruptedException {
        generateJsonForTesting();

        final List<Reservation> reservations = new CopyOnWriteArrayList<>();
        final List<Throwable> exceptions = new CopyOnWriteArrayList<>();

        // Build the SSE client
        var client = RestClientBuilder.newBuilder()
                .baseUri(baseUri)
                .build(SseClient.class);

        // Subscribe to the stream
        Multi<SseEvent<Reservation>> eventStream = client.getEvents();
        eventStream.log().subscribe().with(
                event -> {
                    if (event.data() instanceof Flight flight) {
                        reservations.add(flight);
                    }
                },
                throwable -> {
                    exceptions.add(throwable);
                },
                () -> System.out.println("SSE stream completed"));

        // Initiate the processing
        given().when().accept(ContentType.JSON)
                .contentType(ContentType.JSON)
                .body(new File(ORDER_JSON_FILE))
                .post()
                .then().statusCode(201);

        await().logging()
                .atMost(10, TimeUnit.SECONDS)
                .pollInterval(1000, TimeUnit.MILLISECONDS)
                .until(
                        () -> reservations.size() > 0 || exceptions.size() > 0);

        Assertions.assertThat(reservations).isNotEmpty().anyMatch(r -> r.id.equals(underTest));
        Assertions.assertThat(exceptions).isEmpty();
    }

}
