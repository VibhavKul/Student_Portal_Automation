package utils;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Captures PNG screenshots into <project.folder>/screenshots.
 */
public final class ScreenshotUtil {

    private static final Logger log = LoggerFactory.getLogger(ScreenshotUtil.class);
    private static final DateTimeFormatter TIMESTAMP_FORMAT = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss_SSS");

    private ScreenshotUtil() {
    }

    /**
     * Captures a screenshot for the given driver and saves it under the screenshots folder.
     *
     * @param driver     active WebDriver instance
     * @param scenarioName used to build a readable, unique file name
     * @return absolute path to the saved screenshot file, or null if capture failed
     */
    public static String capture(WebDriver driver, String scenarioName) {
        try {
            String safeName = scenarioName.replaceAll("[^a-zA-Z0-9-_]", "_");
            String fileName = safeName + "_" + LocalDateTime.now().format(TIMESTAMP_FORMAT) + ".png";

            Path screenshotDir = Paths.get(ConfigReader.getInstance().getProjectFolder(), "screenshots");
            Files.createDirectories(screenshotDir);

            Path target = screenshotDir.resolve(fileName);
            File source = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            Files.copy(source.toPath(), target);

            log.info("Screenshot captured: {}", target);
            return target.toAbsolutePath().toString();
        } catch (IOException | ClassCastException e) {
            log.error("Failed to capture screenshot", e);
            return null;
        }
    }
}
