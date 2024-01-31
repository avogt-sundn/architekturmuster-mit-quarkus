package organisationen.bearbeiten;

import java.net.URI;
import java.util.UUID;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.json.bind.Jsonb;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.PATCH;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.PUT;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import lombok.AllArgsConstructor;
import organisationen.suchen.modell.Organisation;

@Path("organizations")
@AllArgsConstructor
@ApplicationScoped
public class OrganisationenBearbeitenResource {

    private static final String FACHSCHLUESSEL_PARAM = "fachschluessel";

    Jsonb jsonb;

    ArbeitsversionMapper mapper;

    @GET
    @Path("{fachschluessel}/draft")

    public Arbeitsversion get(@PathParam(FACHSCHLUESSEL_PARAM) UUID fachschluessel) {

        return mapper.toDomainList(ArbeitsversionEntity.list(FACHSCHLUESSEL_PARAM, fachschluessel)).get(0);
    }

    @POST
    @Path("{fachschluessel}/draft")
    @Transactional
    public Response createArbeitsversion(@PathParam(FACHSCHLUESSEL_PARAM) UUID fachschluessel,
            @Valid Organisation organisation) {

        organisation.setFachschluessel(fachschluessel);
        ArbeitsversionEntity arbeitsversionEntity = ArbeitsversionEntity.builder().fachschluessel(fachschluessel)
                .jsonString(jsonb.toJson(organisation)).build();
        arbeitsversionEntity.persist();

        return Response.ok(mapper.toDomain(arbeitsversionEntity))
                .location(URI.create("/organizations/" + fachschluessel + "/draft")).build();
    }

    // @Operation(summary = "Ändern eine bestehende Arbeitsversion", description =
    // """
    // Mit PUT kann eine unter dem fachschluessel vorliegende Arbeitsversion
    // inhaltlich geändert werden.
    // Die enthaltene Organisation kann bearbeitet werden, ebenso der Status der
    // Arbeitsversion.
    // """)
    @PUT
    @Path("{fachschluessel}/draft")
    @Transactional
    public Response edit(@PathParam(FACHSCHLUESSEL_PARAM) UUID fachschluessel, @Valid Organisation organisation) {
        ArbeitsversionEntity single = ArbeitsversionEntity.find(FACHSCHLUESSEL_PARAM, fachschluessel).firstResult();
        single.jsonString = jsonb.toJson(organisation);
        return Response.ok().build();
    }

    @PATCH
    @Path("{fachschluessel}/draft")
    @Transactional
    @Produces(MediaType.TEXT_PLAIN)
    public Response status(@PathParam(FACHSCHLUESSEL_PARAM) UUID fachschluessel, @Valid Status status) {
        ArbeitsversionEntity single = ArbeitsversionEntity.find(FACHSCHLUESSEL_PARAM, fachschluessel).firstResult();
        single.status = status;
        return Response.ok(status).build();
    }

}
