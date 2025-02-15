package quarkitecture.architecture;

import com.tngtech.archunit.base.DescribedPredicate;
import com.tngtech.archunit.core.domain.Dependency;
import com.tngtech.archunit.core.domain.JavaClass;
import com.tngtech.archunit.lang.ArchCondition;
import com.tngtech.archunit.lang.ConditionEvents;
import com.tngtech.archunit.lang.SimpleConditionEvent;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Test, ob nicht zugelassene Imports vorhanden sind.
 */
final class DependencyCondition extends ArchCondition<JavaClass> {

    private final DescribedPredicate<JavaClass> allowedPackage;

    private final Map<DescribedPredicate<JavaClass>, List<DescribedPredicate<JavaClass>>> additionalAllows =
        new HashMap<>();

    private DependencyCondition(final String... allowedDependencyPackages) {
        super("only have dependencies to " + Arrays.toString(allowedDependencyPackages));
        allowedPackage = JavaClass.Predicates.resideInAnyPackage(allowedDependencyPackages);
    }

    static DependencyCondition onlyHaveDependenciesToPackages(
        final String... allowedDependencyPackages) {
        return new DependencyCondition(allowedDependencyPackages);
    }

    @Override
    public void check(final JavaClass javaClass, final ConditionEvents events) {

        for (final Dependency dependency : javaClass.getDirectDependenciesFromSelf()) {
            if (!allowedPackage.test(dependency.getTargetClass()) && additionalAllows.entrySet()
                .stream().noneMatch(
                    (selector) -> selector.getKey().test(dependency.getOriginClass()) && selector
                        .getValue().stream().anyMatch(p -> p.test(dependency.getTargetClass())))) {
                events.add(SimpleConditionEvent.violated(dependency, dependency.getDescription()));
            }
        }
    }

    public OngoingExceptionalAllow butAllowClassesThat(
        final DescribedPredicate<JavaClass> javaClassDescribedPredicate) {
        return new OngoingExceptionalAllow(javaClassDescribedPredicate);
    }

    /**
     * An ongoing DSL invocation of {@link #butAllowClassesThat(DescribedPredicate)}.
     */
    public class OngoingExceptionalAllow {

        private final DescribedPredicate<JavaClass> javaClassDescribedPredicate;

        OngoingExceptionalAllow(final DescribedPredicate<JavaClass> javaClassDescribedPredicate) {
            this.javaClassDescribedPredicate = javaClassDescribedPredicate;
        }

        public DependencyCondition toDependOnAdditionalPackages(final String... packages) {
            additionalAllows.put(javaClassDescribedPredicate,
                Arrays.stream(packages).map(JavaClass.Predicates::resideInAPackage).toList());
            return DependencyCondition.this;
        }
    }
}
