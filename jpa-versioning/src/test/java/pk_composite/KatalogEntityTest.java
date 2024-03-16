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
class PkCompositeKatalogEntityTest {

    public PkCompositeKatalogEntityTest(Jsonb jsonb) {
        this.jsonb = jsonb;
    }

    Jsonb jsonb;

    PkCompositeKatalogEntity entity = PkCompositeKatalogEntity.builder().eintrag("Armin").isArbeitsversion(false)
            .build();

    @Test
    @Order(1)
    void Create() {
        entity.persist();

        assertThat(entity.id).isNotNull().isNotZero();
        assertThat(entity.isArbeitsversion).isFalse();

    }

    @Test
    @Order(2)
    void AddArbeitsversion() {
        assertThat(entity.id)
                .withFailMessage(
                        "Instanzvariable null! -> " + "bitte ergänze @TestInstance(TestInstance.Lifecycle.PER_CLASS)")
                .isNotNull();

        String json = jsonb.toJson(entity);
        PkCompositeKatalogEntity duplicate = jsonb.fromJson(json, PkCompositeKatalogEntity.class);
        duplicate.isArbeitsversion = true;
        assertThat(duplicate.id).isEqualTo(entity.id);
        duplicate.persist();
        assertThat(duplicate.id).isEqualTo(entity.id);
    }

    @Test
    @Order(3)
    void FindeHauptUndArbeitsversion() {

        PkCompositeKatalogEntity arbeitsversion = PkCompositeKatalogEntity.findById(new KatalogId(entity.id, true));
        assertThat(arbeitsversion.id).isEqualTo(entity.id);
        assertThat(arbeitsversion.isArbeitsversion).isTrue();

        PkCompositeKatalogEntity hauptversion = PkCompositeKatalogEntity.findById(new KatalogId(entity.id, false));
        assertThat(hauptversion.id).isEqualTo(entity.id);
        assertThat(entity.isHauptversion()).isTrue();
    }

    @Test
    @Order(4)
    void FindLiefertBeide() {

        List<PkCompositeKatalogEntity> list = PkCompositeKatalogEntity.find("id", entity.id).list();

        assertThat(list).hasSize(2);

        final Map<Boolean, Long> counts = list.stream().map(item -> item.isArbeitsversion)
                .collect(Collectors.groupingBy(condition -> condition, Collectors.counting()));
        assertThat(counts).withFailMessage("haben wir genau eine Haupt und eine Arbeitsversion?")
                .containsEntry(Boolean.TRUE, 1L).containsEntry(Boolean.FALSE, 1L);

        // count how many items have isArbeitsversion = true
        // read3.stream().map(o->o.isArbeitsversion?)
    }

    @Test
    @Order(5)
    void MergeInExistierendesEntity() {
        PkCompositeKatalogEntity neuEntity = PkCompositeKatalogEntity.builder().eintrag("Johanna")
                .isArbeitsversion(false).build();
        neuEntity.setId(this.entity.id);

        PkCompositeKatalogEntity.getEntityManager().merge(neuEntity);

        PkCompositeKatalogEntity byId = PkCompositeKatalogEntity.findById(new KatalogId(entity.id, false));
        assertThat(byId.id).isNotNull().isEqualTo(this.entity.id);
    }

}
