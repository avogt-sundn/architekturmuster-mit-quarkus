package organisationen.suchen;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

import io.quarkus.panache.common.Page;
import io.quarkus.runtime.util.StringUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.DefaultValue;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.QueryParam;
import jakarta.ws.rs.core.Response;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import organisationen.suchen.modell.OrganisationEntity;
import organisationen.suchen.modell.OrganisationMapper;

@Path("organizations")
@AllArgsConstructor
@ApplicationScoped
@Slf4j
public class OrganisationenSuchenResource {

    OrganisationMapper mapper;

    /**
     * @param id, is a path parameter, like in '/organizations/101'
     * @return the found organization, or null, serialized to '{}'
     * @throws URISyntaxException
     */
    @GET
    @Path("{id}")
    public Response getSingle(@PathParam("id") Long id) throws URISyntaxException {

        return Response.ok(mapper.toDomain(
                OrganisationEntity.findById(id))).location(new URI("/organizations/" + id)).build();
    }

    @GET
    public Response getPaginated(@QueryParam(value = "_page") int page,
            @DefaultValue("10") @QueryParam(value = "_pagesize") int pageSize,
            @DefaultValue("") @QueryParam(value = "beschreibung") String beschreibung,
            @DefaultValue("") @QueryParam(value = "name") String name) {

        log.info("GET with \n\t- page: {}" +
                "\n\t- pagesize: {}" +
                "\n\t- beschreibung: {}", page, pageSize, beschreibung);

        List<OrganisationEntity> list;

        if (!StringUtil.isNullOrEmpty(beschreibung) && !StringUtil.isNullOrEmpty(name)) {
            list = OrganisationEntity.find("beschreibung = ?1 and name =?2", beschreibung, name)
                    .page(Page.of(page, pageSize)).list();
        } else if (!StringUtil.isNullOrEmpty(beschreibung)) {
            list = OrganisationEntity.find("beschreibung", beschreibung)
                    .page(Page.of(page, pageSize)).list();
        } else if (!StringUtil.isNullOrEmpty(name)) {
            list = OrganisationEntity.find("name", name)
                    .page(Page.of(page, pageSize)).list();
        } else {
            list = OrganisationEntity.findAll()
                    .page(Page.of(page, pageSize)).list();
        }

        return Response.ok(mapper.toDomainList(list)).build();
    }

}
