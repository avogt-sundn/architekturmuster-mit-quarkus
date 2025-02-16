package quarkitecture.architecture;

import static com.tngtech.archunit.core.domain.JavaClass.Predicates.resideInAPackage;
import static com.tngtech.archunit.core.domain.JavaClass.Predicates.resideInAnyPackage;
import static com.tngtech.archunit.lang.conditions.ArchConditions.*;
import static com.tngtech.archunit.lang.syntax.ArchRuleDefinition.*;
import static com.tngtech.archunit.library.GeneralCodingRules.*;

import org.junit.jupiter.api.Test;
import org.mapstruct.Mapper;
import org.mapstruct.Qualifier;

import com.tngtech.archunit.base.DescribedPredicate;
import com.tngtech.archunit.core.domain.JavaAnnotation;
import com.tngtech.archunit.core.importer.ImportOption;
import com.tngtech.archunit.junit.AnalyzeClasses;
import com.tngtech.archunit.junit.ArchTest;
import com.tngtech.archunit.lang.ArchRule;
import com.tngtech.archunit.lang.conditions.ArchConditions;

import jakarta.inject.Inject;

//@AnalyzeClasses(packages = "quarkitecture", importOptions = ImportOption.DoNotIncludeTests.class)
final class ArchitectureTest {

    private static final String DE_DEUTSCHE = "de.deutsche.";

    // @ArchTest
    static final ArchRule HEXAGONAL_ARCHITECTURE_IS_RESPECTED;

    // @ArchTest
    static final ArchRule HEXAGONAL_ARCHITECTURE_CIRCUMVENTION_CHECK;

    static {
        final var hexagonalArchitecture = HexagonalArchitectureDefinition.hexagonalArchitectureDefinition()
                .domainModels("..domain..").applicationServices("..application..")
                .adapter("Message-Oriented APIs", "..adapter.mq..")
                .adapter("REST-APIs", "..adapter.rest..").adapter("Storage", "..adapter.storage..")
                .adapter("GRPC-APIs", "..adapter.grpc..")

                .applyAdapterAccessRules();

        HEXAGONAL_ARCHITECTURE_IS_RESPECTED = hexagonalArchitecture.getRule()
                .withOptionalLayers(true).ensureAllClassesAreContainedInArchitectureIgnoring(
                        resideInAnyPackage(DE_DEUTSCHE));

        HEXAGONAL_ARCHITECTURE_CIRCUMVENTION_CHECK = noClasses()
                .should(hexagonalArchitecture.circumventTheArchitecture()).allowEmptyShould(true);
    }

    // @ArchTest
    static final ArchRule DOMAIN_COMPONENTS = classes().that().resideInAnyPackage("de.deutscherv.odv.domain..").should()
            .onlyDependOnClassesThat().resideInAnyPackage("de.deutscherv.odv.domain..", "java..",
                    "lombok..", "com.fasterxml.jackson..", "de.deutscherv.generic.model..",
                    "jakarta.json.bind.annotation..", "org.slf4j..");

    // @ArchTest
    static final ArchRule APPLICATION_COMPONENTS = noClasses().that().haveSimpleNameEndingWith("UseCase").or()
            .haveSimpleNameEndingWith("Port")
            .should().resideOutsideOfPackage("de.deutscherv.odv.application..");

    // @ArchTest
    static final ArchRule CHECK_IMPORTS = classes().should(DependencyCondition
            .onlyHaveDependenciesToPackages(
                    // core and spec
                    "java..", // JLS, JSR 392
                    "jakarta..", // Jakarta EE Core
                    "org.eclipse.microprofile..", // Microprofile
                    "io.opentelemetry..", // OpenTelemetry
                    "io.quarkus.oidc..", // OIDC RestClient
                    "de.deutscherv..",
                    // Allowed dependencies
                    "org.slf4j..", "lombok..", "org.mapstruct..", "com.fasterxml.jackson..",
                    "io.quarkus.hibernate.orm.panache..",
                    "org.hibernate.envers..", "io.quarkus.arc..",
                    "io.hypersistence.utils.hibernate.type.json..", "org.hibernate.annotations..",
                    "io.vertx.ext.web..", "io.quarkus.runtime..", "io.vertx.core.http..",
                    "io.vertx.core.net..", "org.jboss.resteasy.reactive.server", "io.quarkus.scheduler..",
                    "io.smallrye.mutiny..", "javax.annotation..")
            // Java Web Signature Filter
            .butAllowClassesThat(
                    resideInAPackage(DE_DEUTSCHE + ".application.messagefilter.jws"))
            .toDependOnAdditionalPackages("org.jose4j..")
            // Due to AE-086, we need to implement database multi-tenancy. This TIGHTLY
            // BINDS US to Hibernate and Quarkus.
            .butAllowClassesThat(
                    resideInAPackage("de.deutscherv.rvsm.qs.fap.adapter.storage.generic.tenant"))
            .toDependOnAdditionalPackages("io.quarkus.hibernate.orm..", "org.hibernate.engine.jdbc..",
                    "io.agroal..")
            .butAllowClassesThat(
                    resideInAPackage("de.deutscherv.rvsm.qs.fap.adapter.storage.generic.liquibase"))
            .toDependOnAdditionalPackages("io.quarkus.hibernate.orm..", "io.quarkus.liquibase..",
                    "io.quarkus.runtime..",
                    // It seems to be in bug in SmallRye that it does not honor
                    // @ConfigProperty from MP Config in nested
                    "io.smallrye.config..", "liquibase..")
            // Due to security requirements, we need to put the request source IP into the
            // MDC - Context is provided by Vert.x
            .butAllowClassesThat(resideInAPackage("de.deutscherv.rvsm.qs.fap.application.quarkus"))
            .toDependOnAdditionalPackages("io.quarkus..", "io.vertx.core.."));

    // @ArchTest
    static final ArchRule NO_IMPLIED_INJECT;

    static {
        final DescribedPredicate<JavaAnnotation<?>> aQualifier = DescribedPredicate
                .describe("qualifier", a -> a.getRawType().isMetaAnnotatedWith(Qualifier.class));

        NO_IMPLIED_INJECT = noFields().that()
                /* */.areAnnotatedWith("org.eclipse.microprofile.rest.client.inject.RestClient").or()
                .areAnnotatedWith("jakarta.resource.spi.ConfigProperty").or()
                .areAnnotatedWith("org.eclipse.microprofile.config.inject.ConfigProperty").or()
                .areAnnotatedWith("jakarta.ws.rs.core.Context").or()
                .areAnnotatedWith("jakarta.ws.rs.BeanParam").or()
                .areAnnotatedWith("org.eclipse.microprofile.metrics.annotation.Metric").or()
                .areAnnotatedWith("io.smallrye.graphql.client.GraphQLClient").or()
                .areAnnotatedWith(aQualifier)
                /* NB: ArchUnit conditions are left associative, see #213 */
                .and().areNotFinal().should(ArchConditions.not(beAnnotatedWith(Inject.class)))
                .allowEmptyShould(true);
    }

    // @ArchTest
    static final ArchRule NO_FIELD_INJECTION = noFields().that().areDeclaredInClassesThat()
            .areNotMetaAnnotatedWith(Mapper.class).should(BE_ANNOTATED_WITH_AN_INJECTION_ANNOTATION)
            .as("no classes should use field injection").because(
                    "field injection is considered harmful; use constructor injection or setter injection instead; "
                            + "see https://stackoverflow.com/q/39890849 for detailed explanations");

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
