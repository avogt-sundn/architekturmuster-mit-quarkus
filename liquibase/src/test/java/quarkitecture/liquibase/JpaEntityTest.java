package quarkitecture.liquibase;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.transaction.Transactional;

@QuarkusTest
class JpaEntityTest {

    @Test
    @Transactional
    void createEintrag() {

        KatalogEntity entity = new KatalogEntity();
        entity.eintrag = "testing";
        entity.persist();
        assertThat(entity.id).isNotNull();
    }
}
