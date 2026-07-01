package utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Singleton wrapper around ExtentReports. Report lifecycle (init/flush) and
 * per-scenario ExtentTest tracking live here; consumed by the Cucumber
 * event listener that drives the report from Gherkin events.
 */
public final class ExtentReportManager {

    private static final Logger log = LoggerFactory.getLogger(ExtentReportManager.class);
    private static final DateTimeFormatter TIMESTAMP_FORMAT = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");

    private static volatile ExtentReports extent;
    private static String reportPath;
    private static final ThreadLocal<ExtentTest> currentTest = new ThreadLocal<>();

    private ExtentReportManager() {
    }

    public static synchronized ExtentReports getInstance() {
        if (extent == null) {
            extent = createExtentReports();
        }
        return extent;
    }

    private static ExtentReports createExtentReports() {
        ConfigReader config = ConfigReader.getInstance();
        Path reportDir = Paths.get(config.getProjectFolder(), "reports");
        try {
            Files.createDirectories(reportDir);
        } catch (IOException e) {
            throw new IllegalStateException("Could not create Extent report directory: " + reportDir, e);
        }

        String timestamp = LocalDateTime.now().format(TIMESTAMP_FORMAT);
        reportPath = reportDir.resolve("Extent-Report-" + timestamp + ".html").toString();

        ExtentSparkReporter sparkReporter = new ExtentSparkReporter(reportPath);
        sparkReporter.config().setTheme(Theme.STANDARD);
        sparkReporter.config().setDocumentTitle("Student Portal Automation Report");
        sparkReporter.config().setReportName("Student Portal - Cucumber BDD Test Results");

        ExtentReports extentReports = new ExtentReports();
        extentReports.attachReporter(sparkReporter);

        extentReports.setSystemInfo("OS", System.getProperty("os.name") + " " + System.getProperty("os.version"));
        extentReports.setSystemInfo("Browser", config.getBrowser());
        extentReports.setSystemInfo("Environment", config.getAppUrl());
        extentReports.setSystemInfo("Java Version", System.getProperty("java.version"));
        extentReports.setSystemInfo("User", System.getProperty("user.name"));

        log.info("ExtentReports initialized. Report will be written to {}", reportPath);
        return extentReports;
    }

    public static ExtentTest createTest(String name, String description) {
        ExtentTest test = getInstance().createTest(name, description);
        currentTest.set(test);
        return test;
    }

    public static ExtentTest getTest() {
        return currentTest.get();
    }

    public static void removeTest() {
        currentTest.remove();
    }

    public static String getReportPath() {
        return reportPath;
    }

    public static synchronized void flush() {
        if (extent != null) {
            extent.flush();
            log.info("ExtentReports flushed to {}", reportPath);
        }
    }
}
