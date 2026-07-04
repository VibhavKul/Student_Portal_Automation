package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.ConfigReader;

/**
 * Page object for the Student Portal login page (/login).
 */
public class LoginPage extends BasePage {

    private static final Logger log = LoggerFactory.getLogger(LoginPage.class);

    private static final By ERROR_TEXT = By.cssSelector(".auth-card .error-text");
    private static final By FORGOT_PASSWORD_LINK = By.cssSelector(".auth-card button.forgot-password-link");
    private static final By FORGOT_PASSWORD_MODAL = By.cssSelector(".modal-overlay .modal-dialog");
    private static final By FORGOT_PASSWORD_MODAL_MESSAGE = By.cssSelector(".modal-dialog .modal-message");
    private static final By FORGOT_PASSWORD_MODAL_OK_BUTTON = By.cssSelector(".modal-dialog button.btn-primary");

    @FindBy(id = "username")
    private WebElement usernameInput;

    @FindBy(id = "password")
    private WebElement passwordInput;

    @FindBy(css = ".auth-card button.btn-primary")
    private WebElement loginButton;

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    public void navigateToLoginPage() {
        String appUrl = ConfigReader.getInstance().getAppUrl();
        log.info("Navigating to Student Portal login page: {}", appUrl);
        driver.get(appUrl);
        waitUtils.waitForVisible(By.id("username"));
    }

    public void login(String username, String password) {
        log.info("Logging in with username '{}'", username);
        waitUtils.waitForVisible(By.id("username"));
        usernameInput.clear();
        usernameInput.sendKeys(username);
        passwordInput.clear();
        passwordInput.sendKeys(password);
        waitUtils.waitForClickable(By.cssSelector(".auth-card button.btn-primary"));
        loginButton.click();
    }

    public boolean isErrorMessageDisplayed() {
        return waitUtils.isVisible(ERROR_TEXT);
    }

    public String getErrorMessage() {
        return waitUtils.waitForVisible(ERROR_TEXT).getText();
    }

    public boolean isOnLoginPage() {
        return driver.getCurrentUrl().contains("/login");
    }

    public void clickForgotPasswordLink() {
        log.info("Clicking Forgot Password link");
        waitUtils.waitForClickable(FORGOT_PASSWORD_LINK).click();
    }

    public boolean isForgotPasswordModalDisplayed() {
        return waitUtils.isVisible(FORGOT_PASSWORD_MODAL);
    }

    public String getForgotPasswordModalMessage() {
        return waitUtils.waitForVisible(FORGOT_PASSWORD_MODAL_MESSAGE).getText();
    }

    public void closeForgotPasswordModal() {
        log.info("Closing Forgot Password modal");
        waitUtils.waitForClickable(FORGOT_PASSWORD_MODAL_OK_BUTTON).click();
        waitUtils.waitForInvisibility(FORGOT_PASSWORD_MODAL);
    }

    /**
     * Immediate DOM check for absence, not a wait-then-timeout - use only after
     * already confirming closure via waitForInvisibility, otherwise this can
     * false-negative on an element that simply hasn't closed yet.
     */
    public boolean isForgotPasswordModalClosed() {
        return driver.findElements(FORGOT_PASSWORD_MODAL).isEmpty();
    }
}
