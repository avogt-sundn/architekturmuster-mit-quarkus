package json_payload_ohne_bean_binding.field;

import java.lang.reflect.Type;

import jakarta.json.JsonObject;
import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
import jakarta.json.bind.JsonbException;
import jakarta.json.bind.serializer.DeserializationContext;
import jakarta.json.bind.serializer.JsonbDeserializer;
import jakarta.json.bind.serializer.JsonbSerializer;
import jakarta.json.bind.serializer.SerializationContext;
import jakarta.json.stream.JsonGenerator;
import jakarta.json.stream.JsonParser;

/**
 * Ein Feld vom Typ String wird als json serialisiert.
 * Ein json Fragment wird als String deserialisiert.
 *
 * Ohne diesen De-/Serializer wird das json mit escape Symbolen in einem
 * Feld-Wert
 * geschrieben.
 */
public class RawJsonString implements JsonbSerializer<String>, JsonbDeserializer<String> {

    @Override
    public void serialize(String obj, JsonGenerator generator, SerializationContext ctx) {
        // generator.write(obj);
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

    @Override
    public String deserialize(JsonParser parser, DeserializationContext ctx, Type rtType) {
        return parser.getObject().toString();
        // try {
        //     JsonObject object = parser.getObject();
        //     try (Jsonb jsonb = JsonbBuilder.create()) {
        //         return jsonb.toJson(object);
        //     }
        // } catch (JsonbException e) {
        //     throw e;
        // } catch (Exception e) {
        //     e.printStackTrace();
        //     return e.toString();
        // }
    }

}
