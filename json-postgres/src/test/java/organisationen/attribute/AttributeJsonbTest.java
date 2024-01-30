package organisationen.attribute;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.Test;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;

class AttributeJsonbTest {

    @Test
    void serialize() {

        AttributeEntity en = new AttributeEntity();
        en.jsonString = """
                { "test": "testvalue"}
                """;
        Jsonb parser = JsonbBuilder.create();
        String result = parser.toJson(en);
        assertThat(result).doesNotContain("\\");
        assertThat(result).contains("testvalue");

    }
}
