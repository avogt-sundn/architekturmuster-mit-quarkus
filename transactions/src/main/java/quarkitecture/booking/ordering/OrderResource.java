package quarkitecture.booking.ordering;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.jboss.resteasy.reactive.ResponseStatus;

import io.quarkus.logging.Log;
import io.smallrye.mutiny.Multi;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import quarkitecture.booking.domain.Reservation;

@ApplicationScoped
@Path("booking-requests")
public class OrderResource {

    @Channel("orders-outgoing")
    Emitter<Order> orderEmitter; // <1>

    @Channel("reservations")
    Multi<Reservation> effectives;

    /**
     * Endpoint to generate a new quote request id and send it to "quote-requests"
     * AMQP queue using the emitter.
     */
    @POST
    @ResponseStatus(201)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public void createAnOrder(Order order) {
        Log.info("---- order is being forwarded. ----");

        orderEmitter.send(order);
    }

    /**
     * Endpoint retrieving the "quotes" queue and sending the items to a server sent
     * event.
     */
    @GET
    @Produces(MediaType.SERVER_SENT_EVENTS)
    public Multi<Reservation> streamEffectedReservations() {
        return effectives;
    }

}
