package utils;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

/**
 * Thread-safe WebDriver holder. Every test thread gets its own driver instance,
 * keeping the framework ready for parallel execution even though suites run
 * single-threaded today.
 */
public final class DriverManager {

    private static final Logger log = LoggerFactory.getLogger(DriverManager.class);
    private static final ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();

    private DriverManager() {
    }

    public static void initDriver() {
        ConfigReader config = ConfigReader.getInstance();
        String browser = config.getBrowser();

        if (!"chrome".equalsIgnoreCase(browser)) {
            throw new IllegalArgumentException("Unsupported browser: " + browser + ". Only 'chrome' is supported.");
        }

        log.info("Initializing Chrome WebDriver via WebDriverManager");
        WebDriverManager.chromedriver().setup();

        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");

        boolean headless = config.isHeadless();
        if (headless) {
            log.info("Running Chrome in headless mode");
            options.addArguments("--headless=new");
            options.addArguments("--disable-gpu");
            options.addArguments("--window-size=1920,1080");
        }

        WebDriver driver = new ChromeDriver(options);
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(config.getImplicitWait()));
        if (!headless) {
            driver.manage().window().maximize();
        }

        driverThreadLocal.set(driver);
        log.info("WebDriver initialized on thread {}", Thread.currentThread().getId());
    }

    public static WebDriver getDriver() {
        WebDriver driver = driverThreadLocal.get();
        if (driver == null) {
            throw new IllegalStateException("WebDriver has not been initialized for this thread. Was Hooks.setUp() called?");
        }
        return driver;
    }

    public static void quitDriver() {
        WebDriver driver = driverThreadLocal.get();
        if (driver != null) {
            log.info("Quitting WebDriver on thread {}", Thread.currentThread().getId());
            driver.quit();
            driverThreadLocal.remove();
        }
    }
}
