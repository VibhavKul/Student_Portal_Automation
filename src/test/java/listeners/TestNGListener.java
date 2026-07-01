package listeners;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

/**
 * Framework-level TestNG listener. Purely for SLF4J logging of the TestNG
 * test lifecycle (each Cucumber scenario surfaces as one TestNG test method);
 * per-step Extent Report entries are handled separately by
 * {@link ExtentReportListener}.
 */
public class TestNGListener implements ITestListener {

    static {
        // Load config (and set the LOG_DIR system property it exposes) before any
        // Logback logger in this JVM initializes, since logback.xml resolves ${LOG_DIR}.
        utils.ConfigReader.getInstance();
    }

    private static final Logger log = LoggerFactory.getLogger(TestNGListener.class);

    @Override
    public void onStart(ITestContext context) {
        log.info("TestNG suite started: {}", context.getName());
    }

    @Override
    public void onFinish(ITestContext context) {
        log.info("TestNG suite finished: {}. Passed: {}, Failed: {}, Skipped: {}",
                context.getName(),
                context.getPassedTests().size(),
                context.getFailedTests().size(),
                context.getSkippedTests().size());
    }

    @Override
    public void onTestStart(ITestResult result) {
        log.info("Scenario started: {}", result.getMethod().getMethodName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        log.info("Scenario PASSED: {}", result.getMethod().getMethodName());
    }

    @Override
    public void onTestFailure(ITestResult result) {
        log.error("Scenario FAILED: {}", result.getMethod().getMethodName(), result.getThrowable());
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        log.warn("Scenario SKIPPED: {}", result.getMethod().getMethodName());
    }
}
