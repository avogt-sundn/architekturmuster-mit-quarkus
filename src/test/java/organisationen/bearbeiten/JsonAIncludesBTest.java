package organisationen.bearbeiten;

import static org.assertj.core.api.Assertions.*;

import java.util.Map;
import java.util.Random;

import org.junit.jupiter.api.Test;

import com.jayway.jsonpath.DocumentContext;
import com.jayway.jsonpath.JsonPath;

import jakarta.json.bind.Jsonb;
import jakarta.json.bind.JsonbBuilder;
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
    private final static String jsonOrganisation;
    private final static Jsonb jsonb = JsonbBuilder.create();
    private final static Organisation organisation;
    private final static Arbeitsversion arbeitsversion;

    static {

        organisation = TestHelperOrganisation.create();
        organisation.setId(new Random().nextLong());
        arbeitsversion = Arbeitsversion.builder().organisation(organisation).id(
                new Random().nextLong()).build();

        jsonOrganisation = jsonb.toJson(organisation);
        jsonArbeitsversion = jsonb.toJson(arbeitsversion);
    }

    @Test
    void Jayway_JsonPath_1() {

        DocumentContext documentContext = JsonPath.parse(jsonArbeitsversion);
        // selektiere auf das property 'organisation'
        Map<String, Object> jsonpathCreatorName = documentContext.read("$.organisation");
        String json = jsonb.toJson(jsonpathCreatorName);
        Organisation fromJson = jsonb.fromJson(json, Organisation.class);
        assertThat(fromJson).isEqualTo(organisation);
    }
}
