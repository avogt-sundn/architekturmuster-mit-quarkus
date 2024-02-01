package test_protokoll;

import java.util.Formatter;
import java.util.stream.Collectors;

import org.jboss.logging.MDC;
import org.junit.platform.commons.logging.LoggerFactory;
import org.junit.platform.engine.TestExecutionResult;
import org.junit.platform.engine.reporting.ReportEntry;
import org.junit.platform.engine.support.descriptor.MethodSource;
import org.junit.platform.launcher.TestExecutionListener;
import org.junit.platform.launcher.TestIdentifier;
import org.junit.platform.launcher.TestPlan;

public class ShowReportEntries implements TestExecutionListener {

    final org.junit.platform.commons.logging.Logger logger = LoggerFactory.getLogger(ShowReportEntries.class);

    @Override
    public void executionFinished(TestIdentifier testIdentifier, TestExecutionResult testExecutionResult) {

        if (testExecutionResult.getThrowable().isPresent()) {
            if (testIdentifier.getSource().get() instanceof MethodSource ms) {

                String identifier = ms.getJavaClass().getSimpleName() + "#"
                        + ms.getMethodName() + "(" + ms.getMethodParameterTypes() + ")";

                printPerTest(testExecutionResult.getThrowable().get(), ms.getJavaClass(),
                        identifier);
            }
        }

        TestExecutionListener.super.executionFinished(testIdentifier, testExecutionResult);
    }

    @Override
    public void reportingEntryPublished(TestIdentifier testIdentifier, ReportEntry entry) {
        TestExecutionListener.super.reportingEntryPublished(testIdentifier, entry);
    }

    @Override
    public void testPlanExecutionFinished(TestPlan testPlan) {
        logger.info(() -> "testPlanExecutionFinished");
        MDC.clear();
        TestExecutionListener.super.testPlanExecutionFinished(testPlan);
    }

    private void printPerTest(Throwable throwable, Class<?> class1, String string) {

        String collect = MDC.getMap().entrySet().stream().map(e -> {
            try (Formatter f = new Formatter()) {
                return f.format("\t%20s : '%s'", e.getKey(), e.getValue()).toString();
            }
        }).collect(Collectors.joining("\n"));
        MDC.clear();

        final org.junit.platform.commons.logging.Logger logger =
         LoggerFactory.getLogger(class1);

        logger.warn(
                () -> "\n\n  Test " + string + "\n  failed with exception:\n\n  " + throwable + "\n"
                        + (collect.isEmpty() ? ""
                                : ("  Showing MDC collected during the test ('' not part of the value):\n"
                                        + collect.toString())));

    }

}
