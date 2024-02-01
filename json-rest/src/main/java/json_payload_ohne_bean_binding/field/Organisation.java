package json_payload_ohne_bean_binding.field;

import jakarta.json.bind.annotation.JsonbTypeDeserializer;
import jakarta.json.bind.annotation.JsonbTypeSerializer;

/**
 * Domain Objekt.
 *
 * Wird im Rest API genutzt. Steuert die Json De-/Serialisierung.
 */
public class Organisation {

    public String vorname;
    public String nachname;

    @JsonbTypeSerializer(value = RawJsonString.class)
    @JsonbTypeDeserializer(value = RawJsonString.class)
    public String freifeld;
}