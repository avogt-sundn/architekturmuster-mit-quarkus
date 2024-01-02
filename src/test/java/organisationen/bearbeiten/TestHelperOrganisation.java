package organisationen.bearbeiten;

import java.util.stream.IntStream;

import jakarta.enterprise.context.Dependent;
import jakarta.transaction.Transactional;
import jakarta.transaction.Transactional.TxType;
import lombok.AllArgsConstructor;
import organisationen.suchen.modell.AdresseEntity;
import organisationen.suchen.modell.Organisation;
import organisationen.suchen.modell.OrganisationEntity;
import organisationen.suchen.modell.OrganisationMapper;

@Dependent
@AllArgsConstructor
public class TestHelperOrganisation {

    OrganisationMapper mapper;

    private OrganisationEntity create() {
        // erzeuge einen Datensatz in der Datenbank
        OrganisationEntity organisation = OrganisationEntity.builder().beschreibung("Stadtkrankenhaus in Berlin")
                .name("Charité")
                .build();
        AdresseEntity adresse = AdresseEntity.builder().strasse("Charitéplatz 1").postleitzahl(10117)
                .stadt("Berlin")
                .build();
        organisation.addAdresse(adresse);
        return organisation;
    }

    @Transactional(TxType.REQUIRES_NEW)
    public void persistARangeOfZeroToCountEntitiesInTx(int count, String beschreibung) {

        IntStream.range(0, count)
                .map(i -> {
                    persistASingle(beschreibung, i);
                    return i;
                }).sum();
    }

    private OrganisationEntity persistASingle(String beschreibung, int i) {
        OrganisationEntity o = create();
        o.name = "" + i;
        o.beschreibung = beschreibung;
        o.persist();
        return o;
    }

    @Transactional(TxType.REQUIRES_NEW)
    public Organisation persistASingleInTx(String beschreibung, int i) {
        return mapper.toDomain(persistASingle(beschreibung, i));
    }

    @Transactional(TxType.REQUIRES_NEW)
    void deleteById(Long id) {
        OrganisationEntity.deleteById(id);
    }

}
