Feature: Student Portal login and student details submission

  As a student
  I want to log in to the Student Portal and submit my details
  So that my information is recorded and displayed correctly

  Background:
    Given the user is on the Student Portal login page

  @positive @smoke
  Scenario: PBB-000 || TS_01 || Successful login and student details submission flow
    When the user logs in with valid credentials "vibhav.kul" and "password"
    Then the user should land on the Home page
    When the user fills in the student details form with the following valid data and clicks Review
      | fullName         | Vibhav Kulshrestha         |
      | fatherName       | Anil Kulshrestha           |
      | motherMaidenName | Sunita Verma               |
      | studentId        | STU2026001                 |
      | dob              | 2003-05-14                 |
      | email            | vibhav.kul@example.com     |
      | phone            | 9876543210                 |
      | course           | Computer Science           |
      | year             | 3rd                        |
    Then the user should be taken to the Review & Confirm page
    When the user confirms the submission on the Review & Confirm page
    Then the user should be navigated to the Details page
    And the welcome header should display "Welcome, Vibhav Kulshrestha"
    And all submitted details should be correctly displayed on the page

  @positive
  Scenario: PBB-000 || TS_02 || Successful login and student details submission flow with an alternate data set
    When the user logs in with valid credentials "vibhav.kul" and "password"
    Then the user should land on the Home page
    When the user fills in the student details form with the following valid data and clicks Review
      | fullName         | Digamber                   |
      | fatherName       | Rakesh Sharma               |
      | motherMaidenName | Kavita Rao                 |
      | studentId        | STU2026042                 |
      | dob              | 2002-11-30                 |
      | email            | digamber@example.com       |
      | phone            | 9123456780                 |
      | course           | Information Technology     |
      | year             | 2nd                        |
    Then the user should be taken to the Review & Confirm page
    When the user confirms the submission on the Review & Confirm page
    Then the user should be navigated to the Details page
    And the welcome header should display "Welcome, Digamber"
    And all submitted details should be correctly displayed on the page

  @positive @forgotPassword
  Scenario: PBB-786 || TS_01 || Forgot Password link shows a not-implemented popup
    When the user clicks the Forgot Password link
    Then a popup should be displayed with message "The functionality is not yet implemented...!"
    When the user closes the popup
    Then the popup should no longer be displayed
    And the user should remain on the login page

  @negative
  Scenario: PBB-000 || TS_03 || Login failure with invalid credentials
    When the user attempts to log in with invalid credentials "wrong.user" and "wrongpass"
    Then an inline error message "Invalid username or password" should be displayed
    And the user should remain on the login page

  @positive @loginTitle
  Scenario: PBB-999 || TS_01 || Login page displays the "Student Portal" title
    Then the login page title should display "Student Portal"
