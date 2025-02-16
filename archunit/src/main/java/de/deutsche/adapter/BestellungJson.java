package de.deutsche.adapter;

import java.util.List;

import de.deutsche.domain.Bestellung;

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
