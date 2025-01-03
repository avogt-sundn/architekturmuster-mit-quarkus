package quarkitecture.booking.domain;

import java.util.Set;
import java.util.UUID;

/**
 * A set of reservations that comprise a tour requested/confirmed/booked/payed
 * etc. by a customer.
 */
public class Tour {

    public UUID id;
    public Set<Reservation> includedBookings;
    public String customerId;
}
