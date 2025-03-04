package quarkitecture.de.deutsche.adapter.rest;

import java.util.UUID;

import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import quarkitecture.de.deutsche.application.BestellungStornierenUC;

@Path("/bestellungen/stornieren")
@Consumes("application/json")
@Produces("application/json")
class BestellungStornierenResource {

    BestellungStornierenUC bestellungStornierenUC;

    @POST
    public void stornieren(UUID id) {
        bestellungStornierenUC.stornieren(id);
    }
}