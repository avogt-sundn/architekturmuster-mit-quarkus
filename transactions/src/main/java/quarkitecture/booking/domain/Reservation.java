package quarkitecture.booking.domain;

import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

/** Eine Reservierung eines Teiles einer Tour */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "type")
@JsonSubTypes({
        @Type(value = Car.class, name = "car"),
        @Type(value = Hotel.class, name = "hotel"),
        @Type(value = Flight.class, name = "flight")

})
public abstract class Reservation {

    public UUID id;
    public boolean booked;
    
    protected Reservation() {
        this.id = UUID.randomUUID();
    }

    protected Reservation(UUID uuid) {
        this.id = uuid;
    }


}