package quarkitecture.booking.domain;

import java.util.UUID;

public class Car extends Reservation {

    public Car(UUID uuid) {
        super(uuid);
    }

    public Car() {
        super();
    }

}
