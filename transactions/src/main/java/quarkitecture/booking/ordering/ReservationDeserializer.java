package quarkitecture.booking.ordering;

import io.quarkus.kafka.client.serialization.ObjectMapperDeserializer;
import quarkitecture.booking.domain.Reservation;

public class ReservationDeserializer extends ObjectMapperDeserializer<Reservation> {
    public ReservationDeserializer() {
        super(Reservation.class);
    }
}