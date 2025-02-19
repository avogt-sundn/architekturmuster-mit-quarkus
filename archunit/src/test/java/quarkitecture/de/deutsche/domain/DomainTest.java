package quarkitecture.de.deutsche.domain;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;

import quarkitecture.de.deutsche.domain.Bestellung;
import quarkitecture.de.deutsche.domain.Posten;
import quarkitecture.de.deutsche.domain.Produkt;

class DomainTest {
    @Test
    void testNumberOfItems() {
        Bestellung bestellung = new Bestellung();
        assertThat(bestellung.numberOfItems()).isZero();
        bestellung.posten = List.of(new Posten(new Produkt(null, null, 0, 0), 1));
    }
}
