Feature: Review & Confirm page between the Student Details form and the Details page

  As a student
  I want to review everything I entered before it is finalized
  So that I can catch and correct mistakes before my details are submitted

  @positive @reviewConfirm
  Scenario: All entered details are displayed correctly on the Review & Confirm page
    Given the user is on the Home page after logging in
    When the user fills in the student details form with the following valid data and clicks Review
      | fullName         | Vibhav Kulshrestha             |
      | fatherName       | Anil Kulshrestha               |
      | motherMaidenName | Sunita Verma                   |
      | studentId        | STU2026401                     |
      | dob              | 2003-05-14                     |
      | email            | vibhav.kul@example.com         |
      | phone            | 9876543210                     |
      | course           | Computer Science               |
      | year             | 3rd                            |
      | address          | 12 MG Road, Agra, Uttar Pradesh |
    Then the user should be taken to the Review & Confirm page
    And all entered details should be correctly displayed on the Review & Confirm page

  @positive @reviewConfirm
  Scenario: Edit returns to the pre-filled Home form without finalizing the submission
    Given the user is on the Home page after logging in
    When the user fills in the student details form with the following valid data and clicks Review
      | fullName         | Vibhav Kulshrestha             |
      | fatherName       | Anil Kulshrestha               |
      | motherMaidenName | Sunita Verma                   |
      | studentId        | STU2026402                     |
      | dob              | 2003-05-14                     |
      | email            | vibhav.kul@example.com         |
      | phone            | 9876543210                     |
      | course           | Computer Science               |
      | year             | 3rd                            |
      | address          | 12 MG Road, Agra, Uttar Pradesh |
    Then the user should be taken to the Review & Confirm page
    When the user clicks the Edit button on the Review & Confirm page
    Then the Home form should be pre-filled with the previously entered values
    When the user navigates directly to the Details page URL
    Then the user should be redirected back to the Home page

  @positive @reviewConfirm
  Scenario: Confirm & Submit finalizes the details and navigates to the Details page
    Given the user is on the Home page after logging in
    When the user fills in the student details form with the following valid data and clicks Review
      | fullName         | Vibhav Kulshrestha             |
      | fatherName       | Anil Kulshrestha               |
      | motherMaidenName | Sunita Verma                   |
      | studentId        | STU2026403                     |
      | dob              | 2003-05-14                     |
      | email            | vibhav.kul@example.com         |
      | phone            | 9876543210                     |
      | course           | Computer Science               |
      | year             | 3rd                            |
      | address          | 12 MG Road, Agra, Uttar Pradesh |
    Then the user should be taken to the Review & Confirm page
    When the user confirms the submission on the Review & Confirm page
    Then the user should be navigated to the Details page
    And the welcome header should display "Welcome, Vibhav Kulshrestha"
    And all submitted details should be correctly displayed on the page

  @negative @reviewConfirm
  Scenario: Direct navigation to the Details page without going through Review & Confirm is blocked
    Given the user is on the Home page after logging in
    When the user navigates directly to the Details page URL
    Then the user should be redirected back to the Home page
