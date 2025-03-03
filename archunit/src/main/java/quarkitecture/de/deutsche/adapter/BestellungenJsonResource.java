package quarkitecture.de.deutsche.adapter;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import quarkitecture.de.deutsche.application.NeueBestellungAufgebenUC;
import quarkitecture.de.deutsche.domain.Bestellung;

@Path("/bestellungen")
@Consumes("application/json")
@Produces("application/json")
public class BestellungenJsonResource {

    NeueBestellungAufgebenUC neueBestellungAufgebenUC;

    @GET
    public BestellungJson getBestellungen() {
        return BestellungJson.fromDomainModel(neueBestellungAufgebenUC.run(new Bestellung()));
    }
}
