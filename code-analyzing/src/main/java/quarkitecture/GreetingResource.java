package quarkitecture;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;

@Path("/items")
public class GreetingResource {

	@GET
	@Produces(MediaType.TEXT_PLAIN)
	public String getItem() {

		return "Hello from RESTEasy Reactive";
	}
}
