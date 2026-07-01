package listeners;

import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.model.Media;
import io.cucumber.plugin.ConcurrentEventListener;
import io.cucumber.plugin.event.EventPublisher;
import io.cucumber.plugin.event.PickleStepTestStep;
import io.cucumber.plugin.event.Result;
import io.cucumber.plugin.event.TestCase;
import io.cucumber.plugin.event.TestCaseFinished;
import io.cucumber.plugin.event.TestCaseStarted;
import io.cucumber.plugin.event.TestRunFinished;
import io.cucumber.plugin.event.TestStepFinished;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.DriverManager;
import utils.ExtentReportManager;
import utils.ScreenshotUtil;

/**
 * Custom Cucumber plugin that drives ExtentReports directly from Gherkin
 * lifecycle events - one Extent test per Scenario, one log entry per Step,
 * with a screenshot attached automatically after every step.
 *
 * Registered via @CucumberOptions(plugin = {"listeners.ExtentReportListener"}).
 */
public class ExtentReportListener implements ConcurrentEventListener {

    private static final Logger log = LoggerFactory.getLogger(ExtentReportListener.class);

    @Override
    public void setEventPublisher(EventPublisher publisher) {
        publisher.registerHandlerFor(TestCaseStarted.class, this::handleTestCaseStarted);
        publisher.registerHandlerFor(TestStepFinished.class, this::handleTestStepFinished);
        publisher.registerHandlerFor(TestCaseFinished.class, this::handleTestCaseFinished);
        publisher.registerHandlerFor(TestRunFinished.class, this::handleTestRunFinished);
    }

    private void handleTestCaseStarted(TestCaseStarted event) {
        TestCase testCase = event.getTestCase();
        ExtentReportManager.createTest(testCase.getName(), testCase.getUri().toString());
    }

    private void handleTestStepFinished(TestStepFinished event) {
        if (!(event.getTestStep() instanceof PickleStepTestStep)) {
            return; // ignore Before/After hook pseudo-steps
        }
        PickleStepTestStep step = (PickleStepTestStep) event.getTestStep();
        ExtentTest test = ExtentReportManager.getTest();
        if (test == null) {
            return;
        }

        String stepText = step.getStep().getKeyword() + step.getStep().getText();
        Result result = event.getResult();
        Media screenshot = captureStepScreenshot(step.getStep().getText());

        switch (result.getStatus()) {
            case PASSED:
                logStep(test, Status.PASS, stepText, screenshot);
                break;
            case FAILED:
                logStep(test, Status.FAIL, stepText, screenshot);
                if (result.getError() != null) {
                    test.log(Status.FAIL, result.getError());
                }
                break;
            case SKIPPED:
                logStep(test, Status.SKIP, stepText, screenshot);
                break;
            default:
                logStep(test, Status.INFO, stepText, screenshot);
        }
    }

    /**
     * Attaches the screenshot to the same log entry as the step text, via
     * MediaEntityBuilder, so it renders directly under that step in the Spark
     * report instead of floating in a gallery at the top of the test.
     */
    private void logStep(ExtentTest test, Status status, String stepText, Media screenshot) {
        if (screenshot != null) {
            test.log(status, stepText, screenshot);
        } else {
            test.log(status, stepText);
        }
    }

    private Media captureStepScreenshot(String stepName) {
        try {
            String path = ScreenshotUtil.capture(DriverManager.getDriver(), stepName);
            if (path != null) {
                return MediaEntityBuilder.createScreenCaptureFromPath(path).build();
            }
        } catch (Exception e) {
            log.warn("Could not capture screenshot for step '{}'", stepName, e);
        }
        return null;
    }

    private void handleTestCaseFinished(TestCaseFinished event) {
        ExtentReportManager.removeTest();
    }

    private void handleTestRunFinished(TestRunFinished event) {
        ExtentReportManager.flush();
    }
}
