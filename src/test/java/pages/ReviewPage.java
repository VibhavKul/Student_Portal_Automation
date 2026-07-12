package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Page object for the Review &amp; Confirm page (/review) shown between the
 * Student Details form and the final Details page (PBB-803). Displays every
 * entered value read-only and offers "Edit" (back to the pre-filled form)
 * and "Confirm &amp; Submit" (finalizes and navigates to /details).
 */
public class ReviewPage extends BasePage {

    private static final Logger log = LoggerFactory.getLogger(ReviewPage.class);

    private static final By PAGE_TITLE = By.cssSelector(".details-card h2.section-title");
    private static final By EDIT_BUTTON = By.cssSelector("[data-testid='review-edit-button']");
    private static final By CONFIRM_BUTTON = By.cssSelector("[data-testid='review-confirm-button']");

    private static final By FULL_NAME_DISPLAY = By.cssSelector("[data-testid='full-name-review-display']");
    private static final By FATHER_NAME_DISPLAY = By.cssSelector("[data-testid='father-name-review-display']");
    private static final By MOTHER_MAIDEN_NAME_DISPLAY = By.cssSelector("[data-testid='mother-maiden-name-review-display']");
    private static final By STUDENT_ID_DISPLAY = By.cssSelector("[data-testid='student-id-review-display']");
    private static final By DOB_DISPLAY = By.cssSelector("[data-testid='dob-review-display']");
    private static final By EMAIL_DISPLAY = By.cssSelector("[data-testid='email-review-display']");
    private static final By PHONE_DISPLAY = By.cssSelector("[data-testid='phone-review-display']");
    private static final By COURSE_PROGRAM_DISPLAY = By.cssSelector("[data-testid='course-program-review-display']");
    private static final By YEAR_DISPLAY = By.cssSelector("[data-testid='year-review-display']");
    private static final By ADDRESS_DISPLAY = By.cssSelector("[data-testid='address-review-display']");

    public ReviewPage(WebDriver driver) {
        super(driver);
    }

    public boolean isLoaded() {
        return waitUtils.isVisible(CONFIRM_BUTTON) && driver.getCurrentUrl().contains("/review");
    }

    public String getPageTitle() {
        return waitUtils.waitForVisible(PAGE_TITLE).getText();
    }

    public void clickEdit() {
        log.info("Clicking Edit on the Review & Confirm page");
        waitUtils.waitForClickable(EDIT_BUTTON).click();
    }

    public void clickConfirmAndSubmit() {
        log.info("Clicking Confirm & Submit on the Review & Confirm page");
        waitUtils.waitForClickable(CONFIRM_BUTTON).click();
    }

    public String getFullNameDisplay() {
        return readDisplay(FULL_NAME_DISPLAY, "Full Name");
    }

    public String getFatherNameDisplay() {
        return readDisplay(FATHER_NAME_DISPLAY, "Father's Name");
    }

    public String getMotherMaidenNameDisplay() {
        return readDisplay(MOTHER_MAIDEN_NAME_DISPLAY, "Mother's Maiden Name");
    }

    public String getStudentIdDisplay() {
        return readDisplay(STUDENT_ID_DISPLAY, "Student ID");
    }

    public String getDobDisplay() {
        return readDisplay(DOB_DISPLAY, "Date of Birth");
    }

    public String getEmailDisplay() {
        return readDisplay(EMAIL_DISPLAY, "Email");
    }

    public String getPhoneDisplay() {
        return readDisplay(PHONE_DISPLAY, "Phone Number");
    }

    public String getCourseProgramDisplay() {
        return readDisplay(COURSE_PROGRAM_DISPLAY, "Course / Program");
    }

    public String getYearDisplay() {
        return readDisplay(YEAR_DISPLAY, "Year / Semester");
    }

    public String getAddressDisplay() {
        return readDisplay(ADDRESS_DISPLAY, "Address");
    }

    private String readDisplay(By locator, String label) {
        WebElement value = waitUtils.waitForVisible(locator);
        log.info("Read '{}' from Review & Confirm page: '{}'", label, value.getText());
        return value.getText();
    }
}
