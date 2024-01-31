package json_payload_ohne_bean_binding;

import jakarta.json.JsonObject;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.json.bind.JsonbException;
import jakarta.json.bind.serializer.JsonbSerializer;
import jakarta.json.bind.serializer.SerializationContext;
import jakarta.json.stream.JsonGenerator;

/**
 * Ein Feld vom Typ String wird als json serialisiert.
 *
 * Ohne diesen Serializer wird das json mit escape Symbolen in einem Feld-Wert
 * geschrieben.
 */
public class StringWithJsonSerializer implements JsonbSerializer<String> {

    @Override
    public void serialize(String obj, JsonGenerator generator, SerializationContext ctx) {

        try {
            try (Jsonb jsonb = JsonbBuilder.create()) {
                JsonObject result = jsonb.fromJson(obj, JsonObject.class);
                generator.write(result);
            }
        } catch (JsonbException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
