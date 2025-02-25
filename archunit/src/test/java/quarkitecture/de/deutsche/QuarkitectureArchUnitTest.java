package quarkitecture.de.deutsche;

import static com.tngtech.archunit.library.Architectures.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.EvaluationResult;

class QuarkitectureArchUnitTest {

    /**
     * Der Test muss im gleichen Package wie die zu testenden Klassen liegen, damit
     * das WURZEL_PACKAGE stimmt.
     */
    private static final String WURZEL_PACKAGE = QuarkitectureArchUnitTest.class.getPackageName();
    private static final ArchRule myRule = onionArchitecture().domainModels(WURZEL_PACKAGE + "domain..")
            .domainServices(WURZEL_PACKAGE + ".domain..")
            .applicationServices(WURZEL_PACKAGE + ".application..")
            .adapter(WURZEL_PACKAGE + ".adapter..");

    @Test
    void onionTest() {

        JavaClasses importedClasses = new ClassFileImporter().importPackages(
                "quarkitecture.archunit.plantuml.classes");
        EvaluationResult result = myRule.evaluate(importedClasses);

        System.out.println(result.getFailureReport().toString());

        assertTrue(result.filterDescriptionsMatching(
                a -> a.contains(
                        ".domain.BreakingDomainObjekt"))
                .hasViolation());
    }
}
