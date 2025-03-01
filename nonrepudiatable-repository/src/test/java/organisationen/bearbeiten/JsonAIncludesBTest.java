package organisationen.bearbeiten;

import static org.assertj.core.api.Assertions.*;

import java.util.Map;
import java.util.Random;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;

import organisationen.suchen.modell.Organisation;

/**
 * Unit test, der json Implementierungen nutzt.
 * Prüft
 * <ul>
 * <li>Korrektheit der Implementierung</li>
 * <li>Korrektheit im Verständnis beim Entwickler</li>
 * </ul>
 *
 */
class JsonAIncludesBTest {

    private final static String jsonArbeitsversion;
    // private final static String jsonOrganisation;
    private final static ObjectMapper mapper = new ObjectMapper();
    private final static Organisation organisation;
    private final static Arbeitsversion arbeitsversion;

    static {

        mapper.registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());
        mapper.registerModule(new com.fasterxml.jackson.datatype.jdk8.Jdk8Module());
        organisation = TestHelperOrganisation.create();
        organisation.setId(new Random().nextLong());
        arbeitsversion = Arbeitsversion.builder().organisation(organisation).id(
                new Random().nextLong()).build();

        // jsonOrganisation = jsonb.toJson(organisation);
        try {
            jsonArbeitsversion = mapper.writeValueAsString(arbeitsversion);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void Jayway_JsonPath_1() throws JsonProcessingException {

        DocumentContext documentContext = JsonPath.parse(jsonArbeitsversion);
        // selektiere auf das property 'organisation'
        Map<String, Object> jsonpathCreatorName = documentContext.read("$.organisation");
        String json = mapper.writeValueAsString(jsonpathCreatorName);
        Organisation fromJson = mapper.readValue(json, Organisation.class);
        assertThat(fromJson).isEqualTo(organisation);
    }
}
