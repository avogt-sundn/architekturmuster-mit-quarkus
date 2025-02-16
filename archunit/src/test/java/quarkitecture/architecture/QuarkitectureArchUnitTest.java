package quarkitecture.architecture;

import org.junit.jupiter.api.Test;

import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;

@AnalyzeClasses(packages = "de.deutscherv.odv", importOptions = ImportOption.DoNotIncludeTests.class)
class QuarkitectureArchUnitTest {

    private static final String DE_DEUTSCHE = "de.deutsche..";

    /**
     * This method makes Checkstyle pick up on the fact this is actually a Test
     * class.
     */
    @Test
    @SuppressWarnings({ "java:S2699" // method only exists to appease checkstyle.
    })
    void markThisClassAsTest() {
        /**
         * Diese Methode ist mit Absicht leer
         */
    }

}
