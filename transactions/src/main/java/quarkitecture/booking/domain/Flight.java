package quarkitecture.booking.domain;

import java.time.LocalDateTime;
import java.util.UUID;

public class Flight extends Reservation {
    public String departure;
    public String destination;
    public String flightNumber;
    public LocalDateTime departureTime;

    public Flight(UUID uuid) {
        super(uuid);
    }

    public Flight() {
        super();
    }

    public Flight(String flightNumber, String departure, String destination, LocalDateTime departureTime) {
        this.departure = departure;
        this.destination = destination;
        this.flightNumber = flightNumber;
        this.departureTime = departureTime;

    }

}
