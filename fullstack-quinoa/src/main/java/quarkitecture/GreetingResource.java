package quarkitecture;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/hello")
public class GreetingResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Antwort hello() {
        return new Antwort("Hello from RESTEasy Reactive");

    }

}

/**
 * ACHTUNG: nur public Felder werden nach json serialisiert!
 */
class Antwort {
    public Antwort(String string) {
        this.title = string; 
    }

    public String title;
}
