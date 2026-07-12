package stepdefinitions;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.testng.Assert;
import pages.HomePage;
import pages.ReviewPage;
import testdata.StudentTestData;
import utils.ConfigReader;
import utils.DriverManager;
import utils.ScenarioContext;

/**
 * Step definitions for the Review &amp; Confirm page introduced by PBB-803
 * (Home form -> Review &amp; Confirm -> Details). Thin by design: all WebDriver
 * interaction is delegated to ReviewPage/HomePage.
 */
public class ReviewConfirmSteps {

    /** Placeholder the app renders for an empty optional value (e.g. Address). */
    private static final String EMPTY_VALUE_PLACEHOLDER = "—";

    private ReviewPage reviewPage() {
        return new ReviewPage(DriverManager.getDriver());
    }

    private HomePage homePage() {
        return new HomePage(DriverManager.getDriver());
    }

    @Then("the user should be taken to the Review & Confirm page")
    public void the_user_should_be_taken_to_the_review_and_confirm_page() {
        ReviewPage reviewPage = reviewPage();
        Assert.assertTrue(reviewPage.isLoaded(), "Expected to be taken to the Review & Confirm page after clicking Review");
        Assert.assertEquals(reviewPage.getPageTitle(), "Review & Confirm", "Review & Confirm page title mismatch");
    }

    @Then("all entered details should be correctly displayed on the Review & Confirm page")
    public void all_entered_details_should_be_correctly_displayed_on_the_review_and_confirm_page() {
        StudentTestData entered = ScenarioContext.getSubmittedStudentData();
        Assert.assertNotNull(entered, "No entered student data found in scenario context");

        ReviewPage reviewPage = reviewPage();

        Assert.assertEquals(reviewPage.getFullNameDisplay(), entered.getFullName(), "Full Name review display mismatch");
        Assert.assertEquals(reviewPage.getFatherNameDisplay(), entered.getFatherName(), "Father's Name review display mismatch");
        Assert.assertEquals(reviewPage.getMotherMaidenNameDisplay(), entered.getMotherMaidenName(), "Mother's Maiden Name review display mismatch");
        Assert.assertEquals(reviewPage.getStudentIdDisplay(), entered.getStudentId(), "Student ID review display mismatch");
        Assert.assertEquals(reviewPage.getDobDisplay(), entered.getDob(), "Date of Birth review display mismatch");
        Assert.assertEquals(reviewPage.getEmailDisplay(), entered.getEmail(), "Email review display mismatch");
        Assert.assertEquals(reviewPage.getPhoneDisplay(), entered.getPhone(), "Phone Number review display mismatch");
        Assert.assertEquals(reviewPage.getCourseProgramDisplay(), entered.getCourse(), "Course/Program review display mismatch");
        Assert.assertEquals(reviewPage.getYearDisplay(), entered.getYear(), "Year/Semester review display mismatch");

        String expectedAddress = (entered.getAddress() == null || entered.getAddress().trim().isEmpty())
                ? EMPTY_VALUE_PLACEHOLDER
                : entered.getAddress();
        Assert.assertEquals(reviewPage.getAddressDisplay(), expectedAddress, "Address review display mismatch");
    }

    @When("the user confirms the submission on the Review & Confirm page")
    public void the_user_confirms_the_submission_on_the_review_and_confirm_page() {
        reviewPage().clickConfirmAndSubmit();
    }

    @When("the user clicks the Edit button on the Review & Confirm page")
    public void the_user_clicks_the_edit_button_on_the_review_and_confirm_page() {
        reviewPage().clickEdit();
    }

    @Then("the Home form should be pre-filled with the previously entered values")
    public void the_home_form_should_be_pre_filled_with_the_previously_entered_values() {
        StudentTestData entered = ScenarioContext.getSubmittedStudentData();
        Assert.assertNotNull(entered, "No entered student data found in scenario context");

        HomePage homePage = homePage();
        Assert.assertTrue(homePage.isLoaded(), "Expected to be returned to the Home form after clicking Edit");

        Assert.assertEquals(homePage.getFullNameValue(), entered.getFullName(), "Pre-filled Full Name mismatch");
        Assert.assertEquals(homePage.getFatherNameValue(), entered.getFatherName(), "Pre-filled Father's Name mismatch");
        Assert.assertEquals(homePage.getMotherMaidenNameValue(), entered.getMotherMaidenName(), "Pre-filled Mother's Maiden Name mismatch");
        Assert.assertEquals(homePage.getStudentIdValue(), entered.getStudentId(), "Pre-filled Student ID mismatch");
        Assert.assertEquals(homePage.getDobValue(), entered.getDob(), "Pre-filled Date of Birth mismatch");
        Assert.assertEquals(homePage.getEmailValue(), entered.getEmail(), "Pre-filled Email mismatch");
        Assert.assertEquals(homePage.getPhoneValue(), entered.getPhone(), "Pre-filled Phone Number mismatch");
        Assert.assertEquals(homePage.getSelectedCourseProgram(), entered.getCourse(), "Pre-filled Course/Program mismatch");
        Assert.assertEquals(homePage.getSelectedYear(), entered.getYear(), "Pre-filled Year/Semester mismatch");
        if (entered.getAddress() != null && !entered.getAddress().trim().isEmpty()) {
            Assert.assertEquals(homePage.getAddressValue(), entered.getAddress(), "Pre-filled Address mismatch");
        }
    }

    @When("the user navigates directly to the Details page URL")
    public void the_user_navigates_directly_to_the_details_page_url() {
        String detailsUrl = ConfigReader.getInstance().getAppUrl() + "/details";
        DriverManager.getDriver().get(detailsUrl);
    }

    @Then("the user should be redirected back to the Home page")
    public void the_user_should_be_redirected_back_to_the_home_page() {
        Assert.assertTrue(homePage().isLoaded(),
                "Expected to be redirected back to the Home page when the Details page is accessed without confirmed details");
    }
}
