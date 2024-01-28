package organisationen.attribute;

import java.util.Formatter;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.jboss.logging.MDC;
import org.jboss.logmanager.Level;
import org.jboss.logmanager.LogManager;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.TestExecutionExceptionHandler;

/**
 * Zeigt den logging MDC an, falls der UNit Test eine Exception wirft. Dazu muss
 * das log format den Platzhalter %X aufweisen.
 */
class TestBase implements TestExecutionExceptionHandler {

    @Override
    public void handleTestExecutionException(ExtensionContext context, Throwable throwable) throws Throwable {

        final Logger logger = LogManager.getLogManager().getLogger(context.getTestClass().get().getName());
        String collect = MDC.getMap().entrySet().stream().map(e -> {
            String string = new Formatter().format("\t%20s : '%s'", e.getKey(), e.getValue()).toString();
            return string;
        }).collect(Collectors.joining("\n"));
        logger.log(Level.ERROR, "\n  Showing MDC to failed test now:\n" + collect.toString());
        MDC.clear();

        logger.log(Level.INFO, "(Cleared logging MDC on thread, use %X in log format)");

        throw throwable;
    }
}