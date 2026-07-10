package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.Select;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import testdata.StudentTestData;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Page object for the Student Details entry form (/home).
 */
public class HomePage extends BasePage {

    private static final Logger log = LoggerFactory.getLogger(HomePage.class);

    private static final By FATHER_NAME_ERROR = By.xpath(
            "//*[@data-testid='father-name-input']/following-sibling::p[contains(@class,'error-text')]");

    private static final By MOTHER_MAIDEN_NAME_ERROR = By.xpath(
            "//*[@data-testid='mother-maiden-name-input']/following-sibling::p[contains(@class,'error-text')]");

    @FindBy(id = "fullName")
    private WebElement fullNameInput;

    @FindBy(css = "[data-testid='father-name-input']")
    private WebElement fatherNameInput;

    @FindBy(css = "[data-testid='mother-maiden-name-input']")
    private WebElement motherMaidenNameInput;

    @FindBy(id = "studentId")
    private WebElement studentIdInput;

    @FindBy(id = "dob")
    private WebElement dobInput;

    @FindBy(id = "email")
    private WebElement emailInput;

    @FindBy(id = "phone")
    private WebElement phoneInput;

    @FindBy(css = "[data-testid='course-program-select']")
    private WebElement courseProgramSelect;

    @FindBy(id = "year")
    private WebElement yearSelect;

    @FindBy(css = ".details-form button.btn-primary")
    private WebElement submitButton;

    public HomePage(WebDriver driver) {
        super(driver);
    }

    public boolean isLoaded() {
        return waitUtils.isVisible(By.id("fullName")) && driver.getCurrentUrl().contains("/home");
    }

    public void fillStudentDetailsForm(StudentTestData data) {
        log.info("Filling student details form for '{}'", data.getFullName());
        fillFullName(data.getFullName());
        enterFatherName(data.getFatherName());
        enterMotherMaidenName(data.getMotherMaidenName());
        fillFieldsUpToPhone(data);
        selectCourseProgram(data.getCourse());
        fillYear(data.getYear());
    }

    /**
     * Fills every required field except Father's Name, leaving it blank - used by the
     * negative scenarios that assert Submit stays disabled/invalid without one.
     */
    public void fillRequiredFieldsExceptFatherName(StudentTestData data) {
        log.info("Filling student details form (excluding Father's Name) for '{}'", data.getFullName());
        fillFullName(data.getFullName());
        enterMotherMaidenName(data.getMotherMaidenName());
        fillFieldsUpToPhone(data);
        selectCourseProgram(data.getCourse());
        fillYear(data.getYear());
    }

    /**
     * Fills every required field except Mother's Maiden Name, leaving it blank - used by the
     * negative scenarios that assert Submit stays disabled/invalid without one.
     */
    public void fillRequiredFieldsExceptMotherMaidenName(StudentTestData data) {
        log.info("Filling student details form (excluding Mother's Maiden Name) for '{}'", data.getFullName());
        fillFullName(data.getFullName());
        enterFatherName(data.getFatherName());
        fillFieldsUpToPhone(data);
        selectCourseProgram(data.getCourse());
        fillYear(data.getYear());
    }

    /**
     * Fills every required field except Course/Program, leaving the dropdown on its
     * disabled placeholder option - used by the negative scenario that asserts Submit
     * stays disabled without a course selected.
     */
    public void fillRequiredFieldsExceptCourseProgram(StudentTestData data) {
        log.info("Filling student details form (excluding Course/Program) for '{}'", data.getFullName());
        fillFullName(data.getFullName());
        enterFatherName(data.getFatherName());
        enterMotherMaidenName(data.getMotherMaidenName());
        fillFieldsUpToPhone(data);
        fillYear(data.getYear());
    }

    /**
     * Enters a value into the Father's Name field and tabs out of it so the app's
     * onBlur validation runs and any inline error becomes visible.
     */
    public void enterFatherName(String fatherName) {
        waitUtils.waitForVisible(By.cssSelector("[data-testid='father-name-input']"));
        fatherNameInput.clear();
        fatherNameInput.sendKeys(fatherName);
        fatherNameInput.sendKeys(Keys.TAB);
    }

    /**
     * Enters a value into the Mother's Maiden Name field and tabs out of it so the app's
     * onBlur validation runs and any inline error becomes visible.
     */
    public void enterMotherMaidenName(String motherMaidenName) {
        waitUtils.waitForVisible(By.cssSelector("[data-testid='mother-maiden-name-input']"));
        motherMaidenNameInput.clear();
        motherMaidenNameInput.sendKeys(motherMaidenName);
        motherMaidenNameInput.sendKeys(Keys.TAB);
    }

    public boolean isFatherNameErrorDisplayed() {
        return waitUtils.isVisible(FATHER_NAME_ERROR);
    }

    public boolean isMotherMaidenNameErrorDisplayed() {
        return waitUtils.isVisible(MOTHER_MAIDEN_NAME_ERROR);
    }

    public boolean isSubmitButtonEnabled() {
        return submitButton.isEnabled();
    }

    /**
     * Selects a Course/Program option by its visible text (e.g. "Business Administration").
     */
    public void selectCourseProgram(String courseName) {
        waitUtils.waitForVisible(By.cssSelector("[data-testid='course-program-select']"));
        new Select(courseProgramSelect).selectByVisibleText(courseName);
    }

    /**
     * Returns the Course/Program dropdown's selectable option labels, excluding the
     * disabled "-- Select Course/Program --" placeholder.
     */
    public List<String> getCourseProgramOptions() {
        waitUtils.waitForVisible(By.cssSelector("[data-testid='course-program-select']"));
        return new Select(courseProgramSelect).getOptions().stream()
                .filter(WebElement::isEnabled)
                .map(WebElement::getText)
                .collect(Collectors.toList());
    }

    private void fillFullName(String fullName) {
        waitUtils.waitForVisible(By.id("fullName"));
        fullNameInput.clear();
        fullNameInput.sendKeys(fullName);
    }

    private void fillFieldsUpToPhone(StudentTestData data) {
        studentIdInput.clear();
        studentIdInput.sendKeys(data.getStudentId());

        setDateValue(dobInput, data.getDob());

        emailInput.clear();
        emailInput.sendKeys(data.getEmail());

        phoneInput.clear();
        phoneInput.sendKeys(data.getPhone());
    }

    private void fillYear(String year) {
        new Select(yearSelect).selectByVisibleText(year);
    }

    /**
     * Sets a native <input type="date"> value directly via the React-tracked value
     * setter and fires an "input" event, since sendKeys() on a date field is
     * locale-order-dependent (e.g. MM/DD/YYYY in Chrome) rather than ISO order.
     */
    private void setDateValue(WebElement input, String isoDate) {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript(
                "const nativeSetter = Object.getOwnPropertyDescriptor(window.HTMLInputElement.prototype, 'value').set;"
                        + "nativeSetter.call(arguments[0], arguments[1]);"
                        + "arguments[0].dispatchEvent(new Event('input', { bubbles: true }));"
                        + "arguments[0].dispatchEvent(new Event('change', { bubbles: true }));",
                input, isoDate);
    }

    public void submitForm() {
        log.info("Submitting student details form");
        waitUtils.waitForClickable(By.cssSelector(".details-form button.btn-primary"));
        submitButton.click();
    }
}
