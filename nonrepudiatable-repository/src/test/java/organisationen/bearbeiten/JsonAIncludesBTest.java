package organisationen.bearbeiten;

import static org.assertj.core.api.Assertions.*;

import java.util.Map;
import java.util.Random;

import org.junit.jupiter.api.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.InvalidDefinitionException;
import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;

import io.quarkus.test.junit.QuarkusTest;
import jakarta.inject.Inject;
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
@QuarkusTest
class JsonAIncludesBTest {

    private final static Organisation organisation;
    private final static Arbeitsversion arbeitsversion;

    static {

        organisation = TestHelperOrganisation.create();
        organisation.setId(new Random().nextLong());
        arbeitsversion = Arbeitsversion.builder().organisation(organisation).id(
                new Random().nextLong()).build();

    }

    @Test
    void Jayway_JsonPath_1_KeinFramework() throws JsonProcessingException {

        final ObjectMapper mapper = new ObjectMapper();
        /**
         * Konfiguration des mappers ist hier notwendig, weil Jackson direkt genutzt
         * wird.
         */
        mapper.registerModule(new com.fasterxml.jackson.datatype.jsr310.JavaTimeModule());
        mapper.registerModule(new com.fasterxml.jackson.datatype.jdk8.Jdk8Module());


        final String jsonArbeitsversion = mapper.writeValueAsString(arbeitsversion);
        DocumentContext documentContext = JsonPath.parse(jsonArbeitsversion);
        // selektiere auf das property 'organisation'
        Map<String, Object> jsonpathCreatorName = documentContext.read("$.organisation");
        String json = mapper.writeValueAsString(jsonpathCreatorName);
        Organisation fromJson = mapper.readValue(json, Organisation.class);
        assertThat(fromJson).isEqualTo(organisation);
    }

    @Test
    void JackonFailsOnDateField() throws JsonProcessingException {

        final ObjectMapper mapper = new ObjectMapper();
        /**
         * es fehlt die Konfiguration des mappers, weil Jackson direkt genutzt wird.
         */
        Exception expected = null;
        try {
            mapper.writeValueAsString(arbeitsversion);
        } catch (InvalidDefinitionException e) {
            expected = e;
        }
        assertThat(expected).isNotNull();
    }

    @Inject
    ObjectMapper injectedObjectMapper;

    @Test
    void Jayway_JsonPath_1() throws JsonProcessingException {

        /**
         * der jackson objectmapper wird von Quarkus initialisiert! Darum kann er mit
         * Datumsformat umgehen.
         */
        final String jsonArbeitsversion = injectedObjectMapper.writeValueAsString(arbeitsversion);

        DocumentContext documentContext = JsonPath.parse(jsonArbeitsversion);
        // selektiere auf das property 'organisation'
        Map<String, Object> jsonpathCreatorName = documentContext.read("$.organisation");
        String json = injectedObjectMapper.writeValueAsString(jsonpathCreatorName);
        Organisation fromJson = injectedObjectMapper.readValue(json, Organisation.class);
        assertThat(fromJson).isEqualTo(organisation);
    }
}
