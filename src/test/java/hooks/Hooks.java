package hooks;

import io.cucumber.java.After;
import io.cucumber.java.AfterStep;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.DriverManager;
import utils.ScreenshotUtil;

/**
 * Cucumber lifecycle hooks. Driver init/teardown lives here exclusively -
 * never in step definitions or the runner.
 */
public class Hooks {

    private static final Logger log = LoggerFactory.getLogger(Hooks.class);

    private int stepCounter;

    @Before
    public void setUp(Scenario scenario) {
        log.info("Starting scenario: {}", scenario.getName());
        stepCounter = 0;
        DriverManager.initDriver();
    }

    @AfterStep
    public void afterStep(Scenario scenario) {
        stepCounter++;
        WebDriver driver = DriverManager.getDriver();
        String screenshotName = scenario.getName() + "_step" + stepCounter;
        String screenshotPath = ScreenshotUtil.capture(driver, screenshotName);
        if (screenshotPath != null) {
            byte[] screenshotBytes = ((org.openqa.selenium.TakesScreenshot) driver)
                    .getScreenshotAs(org.openqa.selenium.OutputType.BYTES);
            scenario.attach(screenshotBytes, "image/png", screenshotName);
        }
    }

    @After
    public void tearDown(Scenario scenario) {
        log.info("Finished scenario: {} - Status: {}", scenario.getName(), scenario.getStatus());
        DriverManager.quitDriver();
    }
}
