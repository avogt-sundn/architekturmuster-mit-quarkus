package organisationen.attribute;

import static org.assertj.core.api.Assertions.*;

import java.util.UUID;

import org.jboss.logging.MDC;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
import jakarta.json.bind.Jsonb;
import jakarta.transaction.Transactional;

@QuarkusTest
@ExtendWith(TestBase.class)
class AttributeEntityTest {

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
        MDC.put("jsonstring", byKeyValue.jsonString);
        assertThat(byKeyValue.jsonString.toString()).contains("nachname").contains("vogt");
        assertThat(byKeyValue.jsonString.toString()).withFailMessage("json not escaped").doesNotContain("\\");
    }

    @Inject
    Jsonb jsonb;

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
