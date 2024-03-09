package quarkitecture.json.schema;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import org.everit.json.schema.Schema;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SchemaTest {

    final private static String str = """
              {
                "type": "object",
                "properties": {
                  "name": {
                    "type": "string"
                  },
                  "age": {
                    "type": "integer"
                  }
                },
                "required": ["name", "age"]
              }
            """;
    private static Schema schema;

    @BeforeAll
    static void b() throws IOException {
        try (InputStream inputStream = new ByteArrayInputStream(str.getBytes(StandardCharsets.UTF_8))) {
            JSONObject rawSchema = new JSONObject(new JSONTokener(inputStream));
            schema = SchemaLoader.load(rawSchema);
        }
    }

    @Test
    void TesteValides() throws IOException {

        schema.validate(new JSONObject(
                """
                        {
                          "name": "John Doe",
                          "age": 21
                        }
                        """)); // throws a ValidationException if this object is invalid
    }

    @Test
    void TesteFail() throws IOException {
        
        Exception catched = null;
        try {
            schema.validate(new JSONObject(
                    """
                            {
                              "wrongly": "John Doe",
                              "age": 21
                            }
                            """));
        } catch (Exception e) {
            catched = e;
        }
        assertNotNull(catched);
        assertTrue(catched instanceof org.everit.json.schema.ValidationException);
    }
}
