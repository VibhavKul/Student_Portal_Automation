Feature: Father's Name field on the Student Details form

  As a student
  I want the Father's Name field to be captured, validated and displayed correctly
  So that my father's name is accurately recorded on my student profile

  @positive @fatherName
  Scenario: Father's Name is correctly displayed after submission
    Given the user is on the Student Portal login page
    When the user logs in with valid credentials "vibhav.kul" and "password"
    Then the user should land on the Home page
    When the user fills in the student details form with the following valid data and clicks Review
      | fullName         | Vibhav Kulshrestha     |
      | fatherName       | Anil Kulshrestha       |
      | motherMaidenName | Sunita Verma           |
      | studentId        | STU2026101             |
      | dob              | 2003-05-14             |
      | email            | vibhav.kul@example.com |
      | phone            | 9876543210             |
      | course           | Computer Science       |
      | year             | 3rd                    |
    Then the user should be taken to the Review & Confirm page
    When the user confirms the submission on the Review & Confirm page
    Then the user should be navigated to the Details page
    And the Father's Name displayed should be "Anil Kulshrestha"

  @negative @fatherName
  Scenario: Form cannot be submitted with empty Father's Name
    Given the user is on the Home page after logging in
    When the user fills in all required fields except Father's Name with valid data
      | fullName         | Vibhav Kulshrestha     |
      | motherMaidenName | Sunita Verma           |
      | studentId        | STU2026102             |
      | dob              | 2003-05-14             |
      | email            | vibhav.kul@example.com |
      | phone            | 9876543210             |
      | course           | Computer Science       |
      | year             | 3rd                    |
    Then the Submit button should remain disabled

  @negative @fatherName
  Scenario: Father's Name field rejects invalid characters
    Given the user is on the Home page after logging in
    When the user fills in all required fields except Father's Name with valid data
      | fullName         | Vibhav Kulshrestha     |
      | motherMaidenName | Sunita Verma           |
      | studentId        | STU2026103             |
      | dob              | 2003-05-14             |
      | email            | vibhav.kul@example.com |
      | phone            | 9876543210             |
      | course           | Computer Science       |
      | year             | 3rd                    |
    And the user enters "John123" into the Father's Name field
    Then an inline validation error should be displayed for the Father's Name field
    And the Submit button should remain disabled
