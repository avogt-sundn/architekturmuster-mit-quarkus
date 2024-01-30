package organisationen.attribute;

import jakarta.json.JsonObject;
import jakarta.json.bind.JsonbBuilder;
import jakarta.json.bind.serializer.JsonbSerializer;
import jakarta.json.bind.serializer.SerializationContext;
import jakarta.json.stream.JsonGenerator;

public class StringWithJsonSerializer implements JsonbSerializer<String> {

    @Override
    public void serialize(String obj, JsonGenerator generator, SerializationContext ctx) {
        JsonObject result = JsonbBuilder.create().fromJson(obj, JsonObject.class);
        generator.write(result);
    }

}
