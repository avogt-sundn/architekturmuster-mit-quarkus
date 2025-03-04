package quarkitecture.de.deutsche.adapter.jpa;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import quarkitecture.de.deutsche.application.BestellungenRepositoryPO;
import quarkitecture.de.deutsche.domain.Bestellung;

@ApplicationScoped
public class BestellungenRepository implements BestellungenRepositoryPO,
        PanacheRepository<BestellungEntity> {

    @Override
    public Bestellung save(Bestellung bestellung) {

        BestellungEntity orElseGet = find("uid", bestellung.getUid().toString())
                .firstResultOptional().orElseGet(() -> {
                     // fachlichen Schlüssel im entity kopieren
                    BestellungEntity entity2 = new BestellungEntity(bestellung.getUid().toString());
                    persist(entity2);
                    return entity2;
                });
        // kopier Werte aus bestellung zum entity (überschreibend)
        orElseGet.datum = bestellung.getDatum();
        return bestellung;
    }

}
