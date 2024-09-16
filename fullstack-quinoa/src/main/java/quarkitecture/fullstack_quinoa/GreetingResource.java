package quarkitecture.fullstack_quinoa;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/hello")
class GreetingResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Antwort hello() {
        return new Antwort("Hello from RESTEasy Reactive");
    }
}
