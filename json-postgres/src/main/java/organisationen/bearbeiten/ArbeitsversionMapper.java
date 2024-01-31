package organisationen.bearbeiten;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import jakarta.enterprise.context.ApplicationScoped;

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

        return arbeitsversion.build();
    }

    @Mapping(target = "status", ignore = true)
    ArbeitsversionEntity toEntity(Arbeitsversion domain);

}