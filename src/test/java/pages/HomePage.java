package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.Select;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import testdata.StudentTestData;

/**
 * Page object for the Student Details entry form (/home).
 */
public class HomePage extends BasePage {

    private static final Logger log = LoggerFactory.getLogger(HomePage.class);

    @FindBy(id = "fullName")
    private WebElement fullNameInput;

    @FindBy(id = "studentId")
    private WebElement studentIdInput;

    @FindBy(id = "dob")
    private WebElement dobInput;

    @FindBy(id = "email")
    private WebElement emailInput;

    @FindBy(id = "phone")
    private WebElement phoneInput;

    @FindBy(id = "course")
    private WebElement courseInput;

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
        waitUtils.waitForVisible(By.id("fullName"));

        fullNameInput.clear();
        fullNameInput.sendKeys(data.getFullName());

        studentIdInput.clear();
        studentIdInput.sendKeys(data.getStudentId());

        setDateValue(dobInput, data.getDob());

        emailInput.clear();
        emailInput.sendKeys(data.getEmail());

        phoneInput.clear();
        phoneInput.sendKeys(data.getPhone());

        courseInput.clear();
        courseInput.sendKeys(data.getCourse());

        new Select(yearSelect).selectByVisibleText(data.getYear());
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
