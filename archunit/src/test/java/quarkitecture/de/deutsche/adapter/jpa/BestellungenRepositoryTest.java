package quarkitecture.de.deutsche.adapter.jpa;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import quarkitecture.de.deutsche.domain.Bestellung;

@QuarkusTest
class BestellungenRepositoryTest {

    BestellungenRepository repo;

    public BestellungenRepositoryTest(BestellungenRepository repo) {
        this.repo = repo;
    }

    @Test
    void testSave() {

        repo.save(new Bestellung())
    }
}
