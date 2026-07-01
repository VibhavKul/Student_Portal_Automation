package stepdefinitions;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import pages.DetailsPage;
import pages.HomePage;
import testdata.StudentTestData;
import utils.DriverManager;
import utils.ScenarioContext;

import java.util.Map;

/**
 * Step definitions covering the student details form fill/submit and the
 * resulting details summary page. Thin by design: all WebDriver interaction
 * is delegated to HomePage/DetailsPage.
 */
public class StudentDetailsSteps {

    @When("the user fills in the student details form with the following valid data and submits it")
    public void the_user_fills_in_the_student_details_form_and_submits_it(DataTable dataTable) {
        Map<String, String> data = dataTable.asMap(String.class, String.class);

        StudentTestData studentData = StudentTestData.builder()
                .fullName(data.get("fullName"))
                .studentId(data.get("studentId"))
                .dob(data.get("dob"))
                .email(data.get("email"))
                .phone(data.get("phone"))
                .course(data.get("course"))
                .year(data.get("year"))
                .build();

        ScenarioContext.setSubmittedStudentData(studentData);

        WebDriver driver = DriverManager.getDriver();
        HomePage homePage = new HomePage(driver);
        homePage.fillStudentDetailsForm(studentData);
        homePage.submitForm();
    }

    @Then("the user should be navigated to the Details page")
    public void the_user_should_be_navigated_to_the_details_page() {
        DetailsPage detailsPage = new DetailsPage(DriverManager.getDriver());
        Assert.assertTrue(detailsPage.isLoaded(), "Expected to be navigated to the Details page after submitting the form");
    }

    @Then("the welcome header should display {string}")
    public void the_welcome_header_should_display(String expectedHeader) {
        DetailsPage detailsPage = new DetailsPage(DriverManager.getDriver());
        Assert.assertEquals(detailsPage.getWelcomeHeaderText(), expectedHeader, "Welcome header text mismatch");
    }

    @Then("all submitted details should be correctly displayed on the page")
    public void all_submitted_details_should_be_correctly_displayed_on_the_page() {
        StudentTestData submitted = ScenarioContext.getSubmittedStudentData();
        Assert.assertNotNull(submitted, "No submitted student data found in scenario context");

        DetailsPage detailsPage = new DetailsPage(DriverManager.getDriver());

        Assert.assertEquals(detailsPage.getDetailValue("Full Name"), submitted.getFullName(), "Full Name mismatch");
        Assert.assertEquals(detailsPage.getDetailValue("Student ID"), submitted.getStudentId(), "Student ID mismatch");
        Assert.assertEquals(detailsPage.getDetailValue("Date of Birth"), submitted.getDob(), "Date of Birth mismatch");
        Assert.assertEquals(detailsPage.getDetailValue("Email"), submitted.getEmail(), "Email mismatch");
        Assert.assertEquals(detailsPage.getDetailValue("Phone Number"), submitted.getPhone(), "Phone Number mismatch");
        Assert.assertEquals(detailsPage.getDetailValue("Course / Program"), submitted.getCourse(), "Course mismatch");
        Assert.assertEquals(detailsPage.getDetailValue("Year / Semester"), submitted.getYear(), "Year mismatch");

        ScenarioContext.clear();
    }
}
