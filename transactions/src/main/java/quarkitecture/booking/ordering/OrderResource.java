package quarkitecture.booking.ordering;

import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;
import org.jboss.resteasy.reactive.ResponseStatus;
import org.jboss.resteasy.reactive.server.ServerExceptionMapper;

import io.smallrye.mutiny.Multi;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.TransactionScoped;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import quarkitecture.booking.domain.Reservation;
import quarkitecture.booking.domain.Tour;

@ApplicationScoped
@Path("booking-requests")
public class OrderResource {

    @Channel("orders")
    Emitter<Order> orderEmitter; // <1>

    @Channel("reservations")
    Multi<Reservation> effectives;

    @TransactionScoped
    Tour unitOfWork;

    /**
     * Endpoint to generate a new quote request id and send it to "quote-requests"
     * AMQP queue using the emitter.
     */
    @POST
    @Path("/booking")
    @ResponseStatus(201)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public void createAnOrder(Order order) {
        orderEmitter.send(order); // <2>
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

    @ServerExceptionMapper
    public Response toResponse(org.hibernate.exception.ConstraintViolationException exception) {

        return Response.status(Response.Status.CONFLICT)

                .entity("""
                        {
                            "ursache": "Name ist bereits bekannt und damit nicht erneut einlesbar.",
                            "tour": {%s},
                            "sql":"%s"
                        }
                        """
                        .formatted(
                                unitOfWork, exception.getSQL()))
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}
