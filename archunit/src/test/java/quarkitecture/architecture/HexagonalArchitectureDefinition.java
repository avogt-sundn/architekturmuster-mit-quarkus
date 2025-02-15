package quarkitecture.architecture;

import static com.tngtech.archunit.base.DescribedPredicate.or;
import static com.tngtech.archunit.core.domain.Formatters.joinSingleQuoted;
import static com.tngtech.archunit.core.domain.JavaClass.Predicates.resideInAnyPackage;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.tngtech.archunit.base.DescribedPredicate;
import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.lang.ArchCondition;
import com.tngtech.archunit.lang.ConditionEvent;
import com.tngtech.archunit.lang.ConditionEvents;
import com.tngtech.archunit.lang.SimpleConditionEvent;
import com.tngtech.archunit.library.Architectures;

import lombok.Getter;

/**
 * Data-Class / Delegate für {@link Architectures.OnionArchitecture}.
 */
final class HexagonalArchitectureDefinition {

    private static final String DOMAIN_MODEL = "domain model";
    private static final String APPLICATION_SERVICE = "application service";
    private static final String ADAPTER = "adapter";
    private final Architectures.LayeredArchitecture rule;
    @Getter
    private DescribedPredicate<JavaClass> domainModels;
    @Getter
    private DescribedPredicate<JavaClass> applicationServices;
    @Getter
    private final Map<String, DescribedPredicate<JavaClass>> adapters = new HashMap<>();

    private HexagonalArchitectureDefinition() {
        this.rule = Architectures.layeredArchitecture().consideringAllDependencies();
    }

    static HexagonalArchitectureDefinition hexagonalArchitectureDefinition() {
        return new HexagonalArchitectureDefinition();
    }

    Architectures.LayeredArchitecture getRule() {
        return rule;
    }

    public HexagonalArchitectureDefinition domainModels(final String... packageIdentifiers) {
        this.domainModels =
            resideInAnyPackage(packageIdentifiers).as(joinSingleQuoted(packageIdentifiers));
        rule.layer(DOMAIN_MODEL).definedBy(packageIdentifiers);
        return this;
    }

    public HexagonalArchitectureDefinition applicationServices(final String... packageIdentifiers) {
        this.applicationServices =
            resideInAnyPackage(packageIdentifiers).as(joinSingleQuoted(packageIdentifiers));
        rule.layer(APPLICATION_SERVICE).definedBy(packageIdentifiers);
        return this;
    }

    public HexagonalArchitectureDefinition adapter(final String name,
        final String... packageIdentifiers) {
        this.adapters.put(name,
            resideInAnyPackage(packageIdentifiers).as(joinSingleQuoted(packageIdentifiers)));
        rule.layer(name).definedBy(packageIdentifiers).whereLayer(name)
            .mayNotBeAccessedByAnyLayer();
        return this;
    }

    public HexagonalArchitectureDefinition applyAdapterAccessRules() {
        this.getRule().layer(ADAPTER).definedBy(or(getAdapters().values())).whereLayer(DOMAIN_MODEL)
            .mayOnlyBeAccessedByLayers(DOMAIN_MODEL, APPLICATION_SERVICE, ADAPTER)
            .whereLayer(APPLICATION_SERVICE).mayOnlyBeAccessedByLayers(ADAPTER);
        return this;
    }

    public ArchCondition<JavaClass> circumventTheArchitecture() {

        // Deny access from one adapter to another, even when using a Port: That is Business Logic
        // and should be handled by a Service.

        return new ArchCondition<>("circumvent the hexagonal architecture") {

            @Override
            public void check(final JavaClass item, final ConditionEvents events) {

                final var maybeThisAdaper =
                    adapters.entrySet().stream().filter(dp -> dp.getValue().test(item)).findAny();

                if (maybeThisAdaper.isEmpty()) {
                    return;
                }
                final var thisAdapter = maybeThisAdaper.get();

                final var otherAdapters = adapters.entrySet().stream()
                    .filter(e -> !Objects.equals(e.getKey(), thisAdapter.getKey()))
                    .map(Map.Entry::getValue).toList();

                item.getAllFields().stream().filter(field -> field.getRawType().isInterface())
                    .filter(field -> field.getRawType().getAllSubclasses().stream().anyMatch(
                        subclass -> otherAdapters.stream().anyMatch(dp -> dp.test(subclass))))
                    .forEach(field -> {
                        final String message =
                            "has dependency <%s> to another adapter <%s>".formatted(field.getName(),
                                field.getRawType().getAllSubclasses().stream()
                                    .filter(subclass -> otherAdapters.stream()
                                        .anyMatch(dp -> dp.test(subclass)))
                                    .map(JavaClass::getFullName).toList());
                        events.add(SimpleConditionEvent.satisfied(item,
                            ConditionEvent.createMessage(item, message)));
                    });

                item.getAllFields().stream().filter(field -> field.getRawType().isInterface())
                    .filter(field -> applicationServices.test(field.getRawType()))
                    .filter(field -> field.getRawType().getAllSubclasses().stream()
                        .anyMatch(subclass -> thisAdapter.getValue().test(subclass)))
                    .forEach(field -> {
                        final String message =
                            "has dependency <%s> to another port implementation <%s> -> <%s> within the same adapter"
                                .formatted(field.getName(), field.getRawType().getFullName(),
                                    field.getRawType().getAllSubclasses().stream()
                                        .filter(sc -> thisAdapter.getValue().test(sc))
                                        .map(JavaClass::getFullName).toList());
                        events.add(SimpleConditionEvent.satisfied(item,
                            ConditionEvent.createMessage(item, message)));
                    });

            }
        };
    }
}
