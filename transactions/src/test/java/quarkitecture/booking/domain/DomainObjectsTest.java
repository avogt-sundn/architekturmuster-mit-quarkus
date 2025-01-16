package quarkitecture.booking.domain;

import static org.assertj.core.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import quarkitecture.booking.ordering.Order;

class DomainObjectsTest {

    private static final String ORDER_JSON_FILE = "target/test-DomainObjectsTest.json";

    private static final String IDX7710 = "IDX7710";

    @Test
    void useClassHierarchy() {

        {
            Car car = new Car();
            assertThat(car.id).isNotNull();
        }
        {
            UUID myUUID = UUID.randomUUID();
            Car car = new Car(myUUID);
            assertThat(car.id).isEqualTo(myUUID);
        }
    }

    @Test
    void generateJsonForTesting() throws IOException {

        Order order = new Order();
        Tour tour = new Tour();
        order.tour = tour;
        tour.includedBookings = new HashSet<>();
        final LocalDateTime lift = LocalDateTime.of(2025, Month.AUGUST, 28, 5, 20);
        tour.includedBookings.addAll(
                Set.of(
                        new Flight(IDX7710, "PAD", "TLS", lift),
                        new Hotel(), new Car()));

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        objectMapper.registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());
        objectMapper.writeValue(new File(ORDER_JSON_FILE), order);
        File jsonFile = new File(ORDER_JSON_FILE);
        String jsonContent = new String(java.nio.file.Files.readAllBytes(jsonFile.toPath()));
        assertThat(jsonContent).contains("PAD");

        Order value = objectMapper.readValue(jsonContent, Order.class);
        assertThat(value.tour.includedBookings).anyMatch(
                r -> {
                    if (r instanceof Flight f)
                        return f.flightNumber.equals(IDX7710);
                    else
                        return false;
                });

    }
}
