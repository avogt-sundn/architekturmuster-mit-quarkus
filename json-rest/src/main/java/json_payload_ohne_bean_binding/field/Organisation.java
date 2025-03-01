package json_payload_ohne_bean_binding.field;

import com.fasterxml.jackson.annotation.JacksonAnnotation;
import com.fasterxml.jackson.databind.JsonNode;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.extern.jackson.Jacksonized;

/**
 * Domain Objekt.
 *
 * Wird im Rest API genutzt. Steuert die Json De-/Serialisierung.
 */
@Jacksonized
@Builder
@AllArgsConstructor

public class Organisation {
    /**
     * Felder müssen public sein, sonst werden sie bei der Serialisierung ignoriert.
     */
    public String vorname;
    public String nachname;
    public JsonNode freifeld;
}