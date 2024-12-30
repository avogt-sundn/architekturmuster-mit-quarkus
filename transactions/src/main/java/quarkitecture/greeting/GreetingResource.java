package quarkitecture.greeting;

import org.jboss.resteasy.reactive.ResponseStatus;

import com.fasterxml.jackson.databind.JsonNode;

import jakarta.transaction.Transactional;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import quarkitecture.logentry.LogEntry;

@Path("/hello")
public class GreetingResource {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String hello() {
        return "Hello from Quarkus REST";
    }

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
    public Greeting postHello(JsonNode name) {

        Greeting greeting = new Greeting();
        greeting.name = name.get("name").asText();
        greeting.persist();

        LogEntry logEntry = new LogEntry();
        logEntry.message = "Received name: " + name;
        logEntry.persist();

        return greeting;
    }
}
