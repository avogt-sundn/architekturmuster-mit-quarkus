package organisationen.bearbeiten;

import java.net.URI;

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
import organisationen.suchen.modell.Organisation;

@Path("organizations")
@AllArgsConstructor
@ApplicationScoped
public class OrganisationenBearbeitenResource {

    Jsonb jsonb;

    ArbeitsversionMapper mapper;

    @GET
    @Path("{id}/draft")
    public Arbeitsversion get(@PathParam("id") Long organisationId) {

        return mapper.toDomainList(ArbeitsversionEntity.list("organisationId", organisationId)).get(0);
    }

    @POST
    @Path("{id}/draft")
    @Transactional
    public Response createArbeitsversion(@PathParam("id") Long organisationId, @Valid Organisation organisation) {

        ArbeitsversionEntity arbeitsversionEntity = ArbeitsversionEntity.builder().organisationId(organisationId)
                .jsonString(jsonb.toJson(organisation)).build();
        arbeitsversionEntity.persist();

        return Response.ok(mapper.toDomain(arbeitsversionEntity))
                .location(URI.create("/organizations/" + organisationId + "/draft")).build();
    }
}
