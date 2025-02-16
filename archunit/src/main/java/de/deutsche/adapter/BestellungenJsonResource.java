package de.deutsche.adapter;

import de.deutsche.domain.Bestellung;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;

@Path("/bestellungen")
@Consumes("application/json")
@Produces("application/json")
public class BestellungenJsonResource {

    @GET
    public BestellungJson getBestellungen() {
        return BestellungJson.fromDomainModel(new Bestellung());
    }
}
