package stepdefinitions;

import io.cucumber.java.en.Given;
import org.openqa.selenium.WebDriver;
import pages.LoginPage;
import utils.DriverManager;

/**
 * Steps shared across scenarios (currently just the Background navigation step).
 */
public class CommonSteps {

    @Given("the user is on the Student Portal login page")
    public void the_user_is_on_the_student_portal_login_page() {
        WebDriver driver = DriverManager.getDriver();
        new LoginPage(driver).navigateToLoginPage();
    }
}
