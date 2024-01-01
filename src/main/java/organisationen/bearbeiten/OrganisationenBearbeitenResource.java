package organisationen.bearbeiten;

import java.net.URI;
import java.util.List;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.json.bind.Jsonb;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Response;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import organisationen.suchen.modell.Organisation;

@Path("organizations")
@AllArgsConstructor
@ApplicationScoped
@Slf4j
public class OrganisationenBearbeitenResource {

    Jsonb jsonb;

    @GET
    @Path("{id}/draft")
    public List<Arbeitsversion> get(@PathParam("id") Long id) {
        return Arbeitsversion.find("organisationId", id).list();
    }

    @POST
    @Path("{id}/draft")
    @Transactional
    public Response createArbeitsversion(@PathParam("id") Long organisationId, @Valid Organisation organisation) {
        log.info("erzeuge neue arbeitsversion");
        Arbeitsversion.builder().organisationId(organisationId).jsonString(jsonb.toJson(organisation)).build()
                .persist();
        return Response.ok().location(URI.create("/organizations/" + organisationId + "/draft")).build();
    }
}
