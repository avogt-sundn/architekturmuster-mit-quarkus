package quarkitecture.liquibase;

import static org.assertj.core.api.Assertions.*;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.transaction.Transactional;

@QuarkusTest
@TestMethodOrder(OrderAnnotation.class)
@TestInstance(Lifecycle.PER_CLASS)
class JpaEntityTest {

    private Long id = 0L;

    @Test
    @Transactional
    @Order(1)
    void create() {

        KatalogEntity entity = new KatalogEntity();
        entity.eintrag = "testing";
        entity.langerText = RandomStringUtils.randomAlphanumeric(10000);
        entity.persist();
        this.id = entity.id;
        assertThat(entity.id).isNotNull();
    }

    @Test
    @Transactional
    @Order(2)
    void read() {
        KatalogEntity read = KatalogEntity.findById(this.id);
        assertThat(read.langerText.length()).isEqualTo(10000);
        read.delete();
    }
}
