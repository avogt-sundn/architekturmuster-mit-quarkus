package organisationen.attribute;

import static org.assertj.core.api.Assertions.*;

import java.util.UUID;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.transaction.Transactional;

@QuarkusTest
public class AttributeEntityTest {

    @Test
    @Transactional
    void findByKeyValue() {
        UUID id = UUID.randomUUID();
        createFromJson("""
                {
                    "vorname":"armin",
                    "nachname": "vogt",
                    "uuid": "%s"
                }
                """.formatted(id));
        AttributeEntity byKeyValue = AttributeEntity.findFirstByKeyValue("uuid", id.toString());
        assertThat(byKeyValue).isNotNull();
        assertThat(byKeyValue.jsonString).contains("nachname").contains("vogt");

    }

    private void createFromJson(String json) {
        AttributeEntity entity = new AttributeEntity();
        entity.jsonString = json;
        entity.persist();
    }

    @Test
    @Disabled
    @Transactional
    void findByKeyValueInNestedJson() {

        createFromJson("""
                 {
                     "objekt": {
                         "vorname":"heinz"
                   }
                 }
                """);
        AttributeEntity byKeyValue = AttributeEntity.findFirstByKeyValue("vorname", "heinz");
        assertThat(byKeyValue).isNotNull();
    }
}
