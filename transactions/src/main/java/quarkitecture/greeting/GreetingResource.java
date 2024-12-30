package quarkitecture.greeting;

import org.jboss.resteasy.reactive.ResponseStatus;
import org.jboss.resteasy.reactive.server.ServerExceptionMapper;

import com.fasterxml.jackson.databind.JsonNode;

import jakarta.transaction.TransactionScoped;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import quarkitecture.logentry.LogEntry;

@Path("/hello")
public class GreetingResource {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        return "Hello from Quarkus REST";
    }

    @TransactionScoped
    String scoped;

    /**
     *
     * @param name
     * @return
     */
    @POST
    @ResponseStatus(201)
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Transactional
    public Greeting postHello(JsonNode json) {

        final String name = json.get("name").asText();
        scoped = name;
        LogEntry logEntry = new LogEntry();
        logEntry.message = "Empfangen wurde der Name: " + name;
        logEntry.persist();

        try {
            Greeting greeting = new Greeting();
            greeting.name = name;
            greeting.persist();
            return greeting;

        } catch (Exception e) {
            throw new RuntimeException("""
                    // hier wird eine ConstraintViolation nicht gefangen, weil die erst beim commit entsteht!\n"
                               """);

        }
    }

    @ServerExceptionMapper
    public Response toResponse(org.hibernate.exception.ConstraintViolationException exception) {

        return Response.status(Response.Status.CONFLICT)

                .entity("""
                        {
                            "ursache": "Name ist bereits bekannt und damit nicht erneut einlesbar.",
                            "name": "%s",
                            "sql":"%s"
                        }
                        """
                        .formatted(scoped, exception.getSQL()))
                .type(MediaType.APPLICATION_JSON)
                .build();
    }
}
