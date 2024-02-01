package test_protokoll;

import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestExecutionExceptionHandler;
import org.junit.platform.launcher.TestExecutionListener;

/**
 * Zeigt den logging MDC an, falls der UNit Test eine Exception wirft. Dazu muss
 * das log format den Platzhalter %X aufweisen.
 */
class TestBase implements TestExecutionExceptionHandler, TestExecutionListener {

    @Override
    public void handleTestExecutionException(ExtensionContext context, Throwable throwable) throws Throwable {

        // final org.junit.platform.commons.logging.Logger logger =
        // LoggerFactory.getLogger(context.getTestClass().get());

        // String collect = MDC.getMap().entrySet().stream().map(e -> {
        // try (Formatter f = new Formatter()) {
        // return f.format("\t%20s : '%s'", e.getKey(), e.getValue()).toString();
        // }
        // }).collect(Collectors.joining("\n"));

        // logger.error(
        // () -> "\n Showing MDC collected during the test ('' not part of the
        // value):\n" + collect.toString());
        // MDC.clear();

        // logger.error(() -> "(Cleared logging MDC on thread, use %X in log format)");

        throw throwable;
    }
}