package quarkitecture.de.deutsche.domain;

import java.util.List;
import java.util.Optional;

public class Bestellung {
    public List<Posten> posten;

    public int numberOfItems() {

        return Optional.ofNullable(posten).stream().flatMap(List::stream).mapToInt(Posten::anzahl).sum();

    }
}
