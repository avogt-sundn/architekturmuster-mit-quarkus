package quarkitecture;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

/**
 * GreetingResource
 * <p>
 * This class is a RESTful resource that will be used to handle HTTP requests.
 * </p>
 */
@Path("/items")
public class GreetingResource {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public String getItem() {

        return "Hello from RESTEasy Reactive";
    }
}
