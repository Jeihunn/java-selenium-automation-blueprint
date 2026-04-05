@login
@auth
@ui
Feature: Login functionality
  The system should handle both successful and unsuccessful login attempts correctly.

  Background:
    Given the user is on the login page

  @smoke
  @positive
  @severity=blocker
  Scenario: A standard user logs in successfully
    When the user enters "standardUser" as the username
    And the user enters "standardUser" as the password
    And the user clicks the Login button
    Then the user should be on the "Inventory" page

  @regression
  @negative
  @severity=critical
  Scenario Outline: Appropriate error messages are shown for invalid login attempts
    When the user enters "<username>" as the username
    And the user enters "<password>" as the password
    And the user clicks the Login button
    Then the login page should display the error message "<errorMessage>"

    Examples:
      | username      | password      | errorMessage                                                              |
      |               | standardUser  | Epic sadface: Username is required                                        |
      | standardUser  |               | Epic sadface: Password is required                                        |
      | wrong         | wrong         | Epic sadface: Username and password do not match any user in this service |
      | lockedOutUser | lockedOutUser | Epic sadface: Sorry, this user has been locked out.                       |
