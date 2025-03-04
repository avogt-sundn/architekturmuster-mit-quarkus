package quarkitecture.de.deutsche.application;

import java.util.UUID;

public class StornierenService implements BestellungStornierenUC {

    BestellungenRepositoryPO bestellungenRepository;

    @Override
    public void stornieren(UUID bestellung) {
        bestellungenRepository.delete(bestellung);
    }
}