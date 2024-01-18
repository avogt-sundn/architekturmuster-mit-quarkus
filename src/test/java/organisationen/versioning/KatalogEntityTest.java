package organisationen.versioning;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.api.TestMethodOrder;

import io.quarkus.hibernate.orm.panache.PanacheEntityBase;
import io.quarkus.test.junit.QuarkusTest;
import jakarta.json.bind.Jsonb;
import jakarta.transaction.Transactional;

@QuarkusTest
@TestMethodOrder(value = org.junit.jupiter.api.MethodOrderer.OrderAnnotation.class)
@TestInstance(value = Lifecycle.PER_CLASS)
@Transactional
class KatalogEntityTest {

    public KatalogEntityTest(Jsonb jsonb) {
        this.jsonb = jsonb;
    }

    Jsonb jsonb;

    private KatalogEntity katalogEntity = KatalogEntity.builder()
            .eintrag("Armin")
            .isArbeitsversion(false)
            .build();

    @Test
    @Order(1)
    void Create() {

        katalogEntity.persist();
        assertThat(katalogEntity.id).isNotNull();
        assertThat(katalogEntity.isArbeitsversion).isFalse();
    }

    @Test
    @Order(2)
    void AddArbeitsversion() {
        KatalogEntity.getEntityManager().detach(katalogEntity);
        KatalogEntity.getEntityManager().clear();

        String json = jsonb.toJson(katalogEntity);
        KatalogEntity duplicate = jsonb.fromJson(json, KatalogEntity.class);
        duplicate.isArbeitsversion = true;
        assertThat(duplicate.id).isEqualTo(katalogEntity.id);
        duplicate.persist();
        assertThat(duplicate.id).isEqualTo(katalogEntity.id);
    }

    @Test
    @Order(3)
    void FindArbeitsversion() {

        KatalogEntity arbeit = KatalogEntity.findById(
                new KatalogId(katalogEntity.id, true));
        assertThat(arbeit.id).isEqualTo(katalogEntity.id);
        assertThat(katalogEntity.isArbeitsversion).isTrue();

        KatalogEntity keineArbeit = KatalogEntity.findById(
                new KatalogId(katalogEntity.id, false));
        assertThat(keineArbeit.id).isEqualTo(katalogEntity.id);
        assertThat(katalogEntity.isArbeitsversion).isFalse();
    }

    @Test
    @Order(4)
    void FindBeide() {

        List<PanacheEntityBase> read3 = KatalogEntity.find("id", katalogEntity.id).list();
        assertThat(read3.size()).isEqualTo(2);
    }

}
