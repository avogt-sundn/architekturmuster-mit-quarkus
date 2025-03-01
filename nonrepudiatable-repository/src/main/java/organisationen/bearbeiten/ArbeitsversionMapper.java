package organisationen.bearbeiten;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.enterprise.context.ApplicationScoped;
import organisationen.suchen.modell.Organisation;

@Mapper(componentModel = "cdi")
@ApplicationScoped
public interface ArbeitsversionMapper {

    List<Arbeitsversion> toDomainList(List<ArbeitsversionEntity> entities);

    default Arbeitsversion toDomain(ArbeitsversionEntity entity)
            throws JsonProcessingException {

        if (entity == null) {
            return null;
        }

        Arbeitsversion.ArbeitsversionBuilder arbeitsversion = Arbeitsversion.builder();

        arbeitsversion.id(entity.id);
        arbeitsversion.fachschluessel(entity.getFachschluessel());
        ObjectMapper objectMapper = new ObjectMapper();

        Organisation fromJson = objectMapper.readValue(entity.jsonString, Organisation.class);
        arbeitsversion.organisation(fromJson);

        return arbeitsversion.build();
    }

    @Mapping(target = "jsonString", ignore = true)
    @Mapping(target = "status", ignore = true)
    ArbeitsversionEntity toEntity(Arbeitsversion domain);

}