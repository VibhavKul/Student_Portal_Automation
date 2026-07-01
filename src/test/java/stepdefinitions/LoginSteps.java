package stepdefinitions;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import pages.HomePage;
import pages.LoginPage;
import utils.DriverManager;

/**
 * Step definitions covering login (both valid and invalid credential flows).
 * Thin by design: all WebDriver interaction is delegated to LoginPage/HomePage.
 */
public class LoginSteps {

    private LoginPage loginPage() {
        return new LoginPage(DriverManager.getDriver());
    }

    @When("the user logs in with valid credentials {string} and {string}")
    public void the_user_logs_in_with_valid_credentials(String username, String password) {
        loginPage().login(username, password);
    }

    @When("the user attempts to log in with invalid credentials {string} and {string}")
    public void the_user_attempts_to_log_in_with_invalid_credentials(String username, String password) {
        loginPage().login(username, password);
    }

    @Then("the user should land on the Home page")
    public void the_user_should_land_on_the_home_page() {
        WebDriver driver = DriverManager.getDriver();
        HomePage homePage = new HomePage(driver);
        Assert.assertTrue(homePage.isLoaded(), "Expected to land on the Home page after a successful login");
    }

    @Then("an inline error message {string} should be displayed")
    public void an_inline_error_message_should_be_displayed(String expectedMessage) {
        LoginPage loginPage = loginPage();
        Assert.assertTrue(loginPage.isErrorMessageDisplayed(), "Expected an inline error message to be displayed");
        Assert.assertEquals(loginPage.getErrorMessage(), expectedMessage, "Login error message text mismatch");
    }

    @Then("the user should remain on the login page")
    public void the_user_should_remain_on_the_login_page() {
        Assert.assertTrue(loginPage().isOnLoginPage(), "Expected the user to remain on the login page after a failed login");
    }
}
