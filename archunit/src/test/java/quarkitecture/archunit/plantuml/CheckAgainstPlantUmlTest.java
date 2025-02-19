package quarkitecture.archunit.plantuml;

import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.*;
import static com.tngtech.archunit.library.plantuml.rules.PlantUmlArchCondition.*;
import static com.tngtech.archunit.library.plantuml.rules.PlantUmlArchCondition.Configuration.*;
import static org.junit.jupiter.api.Assertions.*;

import java.net.URL;

import org.junit.jupiter.api.Test;

import com.tngtech.archunit.core.domain.JavaClasses;
import com.tngtech.archunit.core.importer.ClassFileImporter;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.EvaluationResult;

/**
 * Hier werden nicht die Junit5 annotations genutzt, weil der Test einen Fehler provozieren soll.
 * Der Test ist also grün, genau wenn die ArchRule rot ist.
 */
class CheckAgainstPlantUmlTest {

    static final URL ARCH_PUML_DATEI = CheckAgainstPlantUmlTest.class.getResource("arch.puml");
    static final ArchRule myRule = classes()
            .should(adhereToPlantUmlDiagram(ARCH_PUML_DATEI, consideringOnlyDependenciesInDiagram()));

    @Test
    void domain_greift_nicht_auf_adapter_zu() {

        JavaClasses importedClasses = new ClassFileImporter().importPackages("quarkitecture.archunit.plantuml.classes");
        EvaluationResult result = myRule.evaluate(importedClasses);

        System.out.println(result.getFailureReport().toString());

        assertTrue(result.filterDescriptionsMatching(
                a -> a.contains(
                        ".domain.BreakingDomainObjekt"))
                .hasViolation());
    }
}
