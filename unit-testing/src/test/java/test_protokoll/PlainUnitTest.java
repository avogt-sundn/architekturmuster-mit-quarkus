package test_protokoll;

import static org.assertj.core.api.Assertions.*;

import org.jboss.logging.MDC;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestReporter;
import org.junit.jupiter.api.extension.ExtendWith;

/**
 * Demonstriert Praktiken und Protokoll
 */
// Test-Klassen sind package-private
@ExtendWith(TestBase.class)
class PlainUnitTest {

    @Test
    void _EinFehlerWirdGeworfen(TestReporter reporter) {

        assertThat(reporter).isNotNull();
        MDC.put("wert", 1);
        reporter.publishEntry("wert war: " + 1);
        throw new RuntimeException();
    }

    @Test
    void _EinAssertScheitert(TestReporter reporter) {

        assertThat(reporter).isNotNull();
        assertThat("inhalt").isBlank();
    }
}