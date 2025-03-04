package quarkitecture.de.deutsche.application;

import quarkitecture.de.deutsche.domain.Bestellung;

public class BestellService implements NeueBestellungAufgebenUC {

    BestellungenRepositoryPO bestellungenRepository;

    @Override
    public Bestellung aufgeben(Bestellung bestellung) {
        return bestellungenRepository.save(bestellung);
    }

}
