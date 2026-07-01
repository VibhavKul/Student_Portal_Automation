package runners;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import listeners.TestNGListener;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Listeners;

/**
 * TestNG entry point for the Cucumber suite. Uses the Cucumber-TestNG
 * integration (AbstractTestNGCucumberTests) rather than Cucumber's native
 * JUnit-style runner, and runs single-threaded (dataProviderThreadCount = 1)
 * today while remaining structurally parallel-ready via DriverManager.
 */
@CucumberOptions(
        features = "src/test/resources/features",
        glue = {"hooks", "stepdefinitions"},
        plugin = {
                "pretty",
                "html:target/cucumber-reports/cucumber.html",
                "json:target/cucumber-reports/cucumber.json",
                "listeners.ExtentReportListener"
        },
        monochrome = true
)
@Listeners(TestNGListener.class)
public class TestNGCucumberRunner extends AbstractTestNGCucumberTests {

    @Override
    @DataProvider(parallel = false)
    public Object[][] scenarios() {
        return super.scenarios();
    }
}
