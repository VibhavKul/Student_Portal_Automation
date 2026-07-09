Feature: Student Portal login and student details submission

  As a student
  I want to log in to the Student Portal and submit my details
  So that my information is recorded and displayed correctly

  Background:
    Given the user is on the Student Portal login page

  @positive @smoke
  Scenario: Successful login and student details submission flow
    When the user logs in with valid credentials "vibhav.kul" and "password"
    Then the user should land on the Home page
    When the user fills in the student details form with the following valid data and submits it
      | fullName         | Vibhav Kulshrestha         |
      | fatherName       | Anil Kulshrestha           |
      | motherMaidenName | Sunita Verma               |
      | studentId        | STU2026001                 |
      | dob              | 2003-05-14                 |
      | email            | vibhav.kul@example.com     |
      | phone            | 9876543210                 |
      | course           | B.Sc Computer Science      |
      | year             | 3rd                        |
    Then the user should be navigated to the Details page
    And the welcome header should display "Welcome, Vibhav Kulshrestha"
    And all submitted details should be correctly displayed on the page

  @positive
  Scenario: Successful login and student details submission flow with an alternate data set
    When the user logs in with valid credentials "vibhav.kul" and "password"
    Then the user should land on the Home page
    When the user fills in the student details form with the following valid data and submits it
      | fullName         | Digamber                   |
      | fatherName       | Rakesh Sharma               |
      | motherMaidenName | Kavita Rao                 |
      | studentId        | STU2026042                 |
      | dob              | 2002-11-30                 |
      | email            | digamber@example.com       |
      | phone            | 9123456780                 |
      | course           | B.Tech Information Technology |
      | year             | 2nd                        |
    Then the user should be navigated to the Details page
    And the welcome header should display "Welcome, Digamber"
    And all submitted details should be correctly displayed on the page

  @positive @forgotPassword
  Scenario: Forgot Password link shows a not-implemented popup (PBB-786)
    When the user clicks the Forgot Password link
    Then a popup should be displayed with message "The functionality is not yet implemented...!"
    When the user closes the popup
    Then the popup should no longer be displayed
    And the user should remain on the login page

  @negative
  Scenario: Login failure with invalid credentials
    When the user attempts to log in with invalid credentials "wrong.user" and "wrongpass"
    Then an inline error message "Invalid username or password" should be displayed
    And the user should remain on the login page
