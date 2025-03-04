package quarkitecture.de.deutsche.application;

import java.util.UUID;

import quarkitecture.de.deutsche.domain.Bestellung;

public interface BestellungenRepositoryPO {

    Bestellung save(Bestellung bestellung);

    void delete(UUID bestellung);

}
