package pk_composite;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.json.bind.Jsonb;
import jakarta.transaction.Transactional;

@QuarkusTest
@Transactional
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class KatalogEntityTest {

    public KatalogEntityTest(Jsonb jsonb) {
        this.jsonb = jsonb;
    }

    Jsonb jsonb;

    KatalogEntity katalogEntity = KatalogEntity.builder().eintrag("Armin").isArbeitsversion(false).build();

    @Test
    @Order(1)
    void Create() {
        katalogEntity.persist();

        assertThat(katalogEntity.id).isNotNull().isNotZero();
        assertThat(katalogEntity.isArbeitsversion).isFalse();

    }

    @Test
    @Order(2)
    void AddArbeitsversion() {
        assertThat(katalogEntity.id)
                .withFailMessage(
                        "Instanzvariable null! -> " + "bitte ergänze @TestInstance(TestInstance.Lifecycle.PER_CLASS)")
                .isNotNull();

        String json = jsonb.toJson(katalogEntity);
        KatalogEntity duplicate = jsonb.fromJson(json, KatalogEntity.class);
        duplicate.isArbeitsversion = true;
        assertThat(duplicate.id).isEqualTo(katalogEntity.id);
        duplicate.persist();
        assertThat(duplicate.id).isEqualTo(katalogEntity.id);
    }

    @Test
    @Order(3)
    void FindeHauptUndArbeitsversion() {

        KatalogEntity arbeitsversion = KatalogEntity.findById(new KatalogId(katalogEntity.id, true));
        assertThat(arbeitsversion.id).isEqualTo(katalogEntity.id);
        assertThat(arbeitsversion.isArbeitsversion).isTrue();

        KatalogEntity hauptversion = KatalogEntity.findById(new KatalogId(katalogEntity.id, false));
        assertThat(hauptversion.id).isEqualTo(katalogEntity.id);
        assertThat(katalogEntity.isHauptversion()).isTrue();
    }

    @Test
    @Order(4)
    void FindLiefertBeide() {

        List<KatalogEntity> list = KatalogEntity.find("id", katalogEntity.id).list();

        assertThat(list).hasSize(2);

        final Map<Boolean, Long> counts = list.stream().map(item -> item.isArbeitsversion)
                .collect(Collectors.groupingBy(condition -> condition, Collectors.counting()));
        assertThat(counts).withFailMessage("haben wir genau eine Haupt und eine Arbeitsversion?")
                .containsEntry(Boolean.TRUE, 1L).containsEntry(Boolean.FALSE, 1L);

        // count how many items have isArbeitsversion = true
        // read3.stream().map(o->o.isArbeitsversion?)
    }

}
