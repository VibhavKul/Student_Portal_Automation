package stepdefinitions;

import io.cucumber.java.en.Given;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import pages.HomePage;
import pages.LoginPage;
import utils.DriverManager;

/**
 * Steps shared across scenarios (currently just the Background navigation step
 * and the combined login-and-land-on-Home step used by the Father's Name scenarios).
 */
public class CommonSteps {

    @Given("the user is on the Student Portal login page")
    public void the_user_is_on_the_student_portal_login_page() {
        WebDriver driver = DriverManager.getDriver();
        new LoginPage(driver).navigateToLoginPage();
    }

    @Given("the user is on the Home page after logging in")
    public void the_user_is_on_the_home_page_after_logging_in() {
        WebDriver driver = DriverManager.getDriver();
        LoginPage loginPage = new LoginPage(driver);
        loginPage.navigateToLoginPage();
        loginPage.login("vibhav.kul", "password");

        HomePage homePage = new HomePage(driver);
        Assert.assertTrue(homePage.isLoaded(), "Expected to land on the Home page after logging in");
    }
}
