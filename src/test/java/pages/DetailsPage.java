package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Page object for the submitted Student Details summary page (/details).
 */
public class DetailsPage extends BasePage {

    private static final Logger log = LoggerFactory.getLogger(DetailsPage.class);

    private static final By FATHER_NAME_DISPLAY = By.cssSelector("[data-testid='father-name-display']");

    @FindBy(css = ".details-card h2.section-title")
    private WebElement welcomeHeader;

    public DetailsPage(WebDriver driver) {
        super(driver);
    }

    public boolean isLoaded() {
        return waitUtils.isVisible(By.cssSelector(".details-card")) && driver.getCurrentUrl().contains("/details");
    }

    public String getWelcomeHeaderText() {
        waitUtils.waitForVisible(By.cssSelector(".details-card h2.section-title"));
        return welcomeHeader.getText();
    }

    public String getFatherNameDisplay() {
        WebElement value = waitUtils.waitForVisible(FATHER_NAME_DISPLAY);
        log.info("Read Father's Name from Details page: '{}'", value.getText());
        return value.getText();
    }

    /**
     * Reads the value shown next to the given field label (e.g. "Full Name", "Student ID").
     */
    public String getDetailValue(String label) {
        By locator = By.xpath("//dl[contains(@class,'details-list')]//dt[normalize-space(text())='"
                + label + "']/following-sibling::dd[1]");
        WebElement value = waitUtils.waitForVisible(locator);
        log.info("Read details page value for '{}': '{}'", label, value.getText());
        return value.getText();
    }
}
