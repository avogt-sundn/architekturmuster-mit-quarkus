package quarkitecture.booking.domain;

import java.util.UUID;

public final class Hotel extends Reservation {

    public Hotel(UUID uuid) {
        super(uuid);
    }

    public Hotel() {
        super();
    }

}