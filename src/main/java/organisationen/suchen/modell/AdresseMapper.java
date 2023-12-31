package organisationen.suchen.modell;

import java.util.List;

import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

@Mapper(componentModel = "cdi")
public interface AdresseMapper {

    List<Adresse> toDomainList(List<AdresseEntity> entities);

    Adresse toDomain(AdresseEntity entity);

    @InheritInverseConfiguration(name = "toDomain")
    AdresseEntity toEntity(Adresse domain);

    void updateEntityFromDomain(Adresse domain, @MappingTarget AdresseEntity entity);

    void updateDomainFromEntity(AdresseEntity entity, @MappingTarget Adresse domain);

};
