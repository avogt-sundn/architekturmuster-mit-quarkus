package quarkitecture.de.deutsche.adapter;

import quarkitecture.de.deutsche.domain.Posten;

public record PostenJson(String produkt, int anzahl) {

    public static PostenJson fromDomainModel(Posten posten) {
        return new PostenJson(
                posten.produkt().name(),
                posten.anzahl());
    }
}
