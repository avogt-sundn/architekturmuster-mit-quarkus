package organisationen.suchen.modell;

import java.util.List;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "cdi")
public interface OrganisationMapper {

    List<Organisation> toDomainList(List<OrganisationEntity> entities);

    Organisation toDomain(OrganisationEntity entity);

    @InheritInverseConfiguration(name = "toDomain")
    OrganisationEntity toEntity(Organisation domain);

    void updateEntityFromDomain(Organisation domain, @MappingTarget OrganisationEntity entity);

    void updateDomainFromEntity(OrganisationEntity entity, @MappingTarget Organisation domain);

}