package organisationen.bearbeiten;

import java.net.URI;
import java.util.UUID;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.json.bind.Jsonb;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Response;
import lombok.AllArgsConstructor;
import organisationen.suchen.modell.Organisation;

@Path("organizations")
@AllArgsConstructor
@ApplicationScoped
public class OrganisationenBearbeitenResource {

    Jsonb jsonb;

    ArbeitsversionMapper mapper;

    @GET
    @Path("{fachschluessel}/draft")
    public Arbeitsversion get(@PathParam("fachschluessel") UUID fachschluessel) {

        return mapper.toDomainList(ArbeitsversionEntity.list("fachschluessel", fachschluessel)).get(0);
    }

    @POST
    @Path("{fachschluessel}/draft")
    @Transactional
    public Response createArbeitsversion(@PathParam("fachschluessel") UUID fachschluessel,
            @Valid Organisation organisation) {

        organisation.setFachschluessel(fachschluessel);
        ArbeitsversionEntity arbeitsversionEntity = ArbeitsversionEntity.builder().fachschluessel(fachschluessel)
                .jsonString(jsonb.toJson(organisation)).build();
        arbeitsversionEntity.persist();

        return Response.ok(mapper.toDomain(arbeitsversionEntity))
                .location(URI.create("/organizations/" + fachschluessel + "/draft")).build();
    }

    @PUT
    @Path("{fachschluessel}/draft")
    @Transactional
    public Response freigeben(@PathParam("fachschluessel") UUID fachschluessel, @Valid Organisation organisation) {
        ArbeitsversionEntity single = ArbeitsversionEntity.find("fachschluessel", fachschluessel).firstResult();
        single.jsonString = jsonb.toJson(organisation);
        return Response.ok().build();
    }

}
