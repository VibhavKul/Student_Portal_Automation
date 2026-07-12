Feature: Course/Program dropdown on the Student Details form

  As a student
  I want to select my Course/Program from a predefined dropdown
  So that only valid course names are recorded on my student profile

  @positive @courseProgram
  Scenario: PBB-802 || TS_01 || Selected Course/Program is correctly displayed after submission
    Given the user is on the Student Portal login page
    When the user logs in with valid credentials "vibhav.kul" and "password"
    Then the user should land on the Home page
    When the user fills in the student details form with the following valid data and clicks Review
      | fullName         | Vibhav Kulshrestha         |
      | fatherName       | Anil Kulshrestha           |
      | motherMaidenName | Sunita Verma               |
      | studentId        | STU2026301                 |
      | dob              | 2003-05-14                 |
      | email            | vibhav.kul@example.com     |
      | phone            | 9876543210                 |
      | course           | Business Administration    |
      | year             | 3rd                        |
    Then the user should be taken to the Review & Confirm page
    When the user confirms the submission on the Review & Confirm page
    Then the user should be navigated to the Details page
    And the Details page should display "Business Administration" as the Course/Program

  @negative @courseProgram
  Scenario: PBB-802 || TS_02 || Form cannot be submitted without selecting a Course/Program
    Given the user is on the Home page after logging in
    When the user fills in all required fields except Course/Program with valid data
      | fullName         | Vibhav Kulshrestha     |
      | fatherName       | Anil Kulshrestha       |
      | motherMaidenName | Sunita Verma           |
      | studentId        | STU2026302             |
      | dob              | 2003-05-14             |
      | email            | vibhav.kul@example.com |
      | phone            | 9876543210             |
      | year             | 3rd                    |
    Then the Submit button should remain disabled

  @positive @courseProgram
  Scenario: PBB-802 || TS_03 || All expected Course/Program options are present in the dropdown
    Given the user is on the Home page after logging in
    When the user opens the Course/Program dropdown
    Then the Course/Program dropdown should contain exactly these options: "Computer Science, Information Technology, Electronics & Communication, Mechanical Engineering, Civil Engineering, Business Administration, Commerce, Biotechnology, Mathematics, Physics"
