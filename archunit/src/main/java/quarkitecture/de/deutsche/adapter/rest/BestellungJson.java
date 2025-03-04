package quarkitecture.de.deutsche.adapter.rest;

import java.util.List;

import quarkitecture.de.deutsche.domain.Bestellung;

public record BestellungJson(
        List<PostenJson> posten,
        int numberOfItems) {

    static BestellungJson fromDomainModel(Bestellung cart) {
        return new BestellungJson(
                cart.posten.stream().map(
                        PostenJson::fromDomainModel).toList(),
                cart.numberOfItems());
    }
}
