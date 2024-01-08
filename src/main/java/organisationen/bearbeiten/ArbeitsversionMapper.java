package organisationen.bearbeiten;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import organisationen.suchen.modell.Organisation;

@Mapper(componentModel = "cdi")
@ApplicationScoped
public interface ArbeitsversionMapper {

    List<Arbeitsversion> toDomainList(List<ArbeitsversionEntity> entities);

    default Arbeitsversion toDomain(ArbeitsversionEntity entity) {
        if (entity == null) {
            return null;
        }

        Arbeitsversion.ArbeitsversionBuilder arbeitsversion = Arbeitsversion.builder();

        arbeitsversion.id(entity.id);
        arbeitsversion.fachschluessel(entity.getFachschluessel());
        try (Jsonb jsonb = JsonbBuilder.create()) {
            Organisation fromJson = jsonb.fromJson(entity.jsonString, Organisation.class);
            arbeitsversion.organisation(fromJson);
        } catch (Exception e) {
            Log.error(e);
        }

        return arbeitsversion.build();
    }

    @Mapping(target = "jsonString", ignore = true)
    @Mapping(target = "status", ignore = true)
    ArbeitsversionEntity toEntity(Arbeitsversion domain);

}