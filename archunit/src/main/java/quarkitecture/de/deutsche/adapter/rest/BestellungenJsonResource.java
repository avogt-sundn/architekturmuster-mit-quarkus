package quarkitecture.de.deutsche.adapter.rest;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import quarkitecture.de.deutsche.application.BestellungStornierenUC;
import quarkitecture.de.deutsche.application.NeueBestellungAufgebenUC;
import quarkitecture.de.deutsche.domain.Bestellung;

@Path("/bestellungen")
@Consumes("application/json")
@Produces("application/json")
class BestellungenJsonResource {

    NeueBestellungAufgebenUC neueBestellungAufgebenUC;

    @GET
    public BestellungJson getBestellungen() {
        return BestellungJson.fromDomainModel(neueBestellungAufgebenUC.aufgeben(new Bestellung()));
    }
}


