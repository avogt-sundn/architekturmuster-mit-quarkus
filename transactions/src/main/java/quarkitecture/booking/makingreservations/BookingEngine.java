package quarkitecture.booking.makingreservations;

import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;

import io.quarkus.logging.Log;
import io.smallrye.mutiny.Multi;
import io.smallrye.reactive.messaging.annotations.Blocking;
import jakarta.enterprise.context.ApplicationScoped;
import quarkitecture.booking.domain.Reservation;
import quarkitecture.booking.ordering.Order;

/**
 * A bean consuming data from the "request" AMQP queue and giving out a random
 * quote.
 * The result is pushed to the "quotes" AMQP queue.
 */
@ApplicationScoped
public class BookingEngine {

    @Incoming("orders")
    @Outgoing("reservations")
    @Blocking
    public Reservation process(Order order) {

        Log.info("---- Request is being answered. ----");
        return order.tour.includedBookings.iterator().next();
    }
}