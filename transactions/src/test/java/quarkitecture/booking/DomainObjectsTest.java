package quarkitecture.booking;

import static org.assertj.core.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.fasterxml.jackson.core.exc.StreamWriteException;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import quarkitecture.booking.domain.Car;
import quarkitecture.booking.domain.Flight;
import quarkitecture.booking.domain.Hotel;
import quarkitecture.booking.domain.Tour;

class DomainObjectsTest {

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

    @ParameterizedTest
    @ValueSource(strings = { "request.json", })
    void generateJsonForTesting(String fileName) throws StreamWriteException, DatabindException, IOException {

        Tour tour = new Tour();
        tour.includedBookings = new HashSet<>();
        final LocalDateTime lift = LocalDateTime.of(2025, Month.AUGUST, 28, 5, 20);
        tour.includedBookings.addAll(
                Set.of(
                        new Flight(IDX7710, "PAD", "TLS", lift),
                        new Hotel(), new Car()));
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        objectMapper.registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());
        objectMapper.writeValue(new File("json"), tour);
        File jsonFile = new File("json");
        String jsonContent = new String(java.nio.file.Files.readAllBytes(jsonFile.toPath()));
        assertThat(jsonContent).contains("PAD");

        Tour value = objectMapper.readValue(jsonContent, Tour.class);
        assertThat(value.includedBookings).anyMatch(
                r -> {
                    if (r instanceof Flight f)
                        return f.flightNumber.equals(IDX7710);
                    else
                        return false;
                });

    }
}
