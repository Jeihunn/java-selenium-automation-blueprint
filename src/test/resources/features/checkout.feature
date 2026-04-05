@checkout
@ui
Feature: Checkout flow functionality
  The user should be able to enter checkout information, review the order
  summary, and complete the purchase successfully.

  Background:
    Given the user is on the login page
    When the user enters "standardUser" as the username
    And the user enters "standardUser" as the password
    And the user clicks the Login button
    And the user adds the following products to the cart
      | Sauce Labs Backpack      |
      | Sauce Labs Fleece Jacket |
    And the user opens the cart page
    And the user clicks the "Checkout" button on the cart page
    And the user should be on the "Checkout: Your Information" page

  @smoke
  @positive
  @severity=blocker
  Scenario: The user completes the order successfully with valid checkout information
    When the user enters "RANDOM" into the "First Name" field on the checkout page
    And the user enters "RANDOM" into the "Last Name" field on the checkout page
    And the user enters "RANDOM" into the "Zip/Postal Code" field on the checkout page
    And the user clicks the "Continue" button on the checkout page
    Then the user should be on the "Checkout: Overview" page
    When the user clicks the "Finish" button on the checkout page
    Then the checkout complete page should display the message "Thank you for your order!"
    And the cart badge should show 0
    When the user clicks the "Back Home" button on the checkout page
    Then the user should be on the "Inventory" page

  @regression
  @negative
  @severity=critical
  Scenario Outline: Appropriate error messages are shown when checkout information is incomplete
    When the user enters "<firstName>" into the "First Name" field on the checkout page
    And the user enters "<lastName>" into the "Last Name" field on the checkout page
    And the user enters "<zipCode>" into the "Zip/Postal Code" field on the checkout page
    And the user clicks the "Continue" button on the checkout page
    Then the checkout page should display the error message "<errorMessage>"

    Examples:
      | firstName | lastName | zipCode | errorMessage                   |
      |           | RANDOM   | RANDOM  | Error: First Name is required  |
      | RANDOM    |          | RANDOM  | Error: Last Name is required   |
      | RANDOM    | RANDOM   |         | Error: Postal Code is required |

  @regression
  @positive
  @severity=normal
  Scenario: The checkout overview displays the correct products and totals
    When the user enters "RANDOM" into the "First Name" field on the checkout page
    And the user enters "RANDOM" into the "Last Name" field on the checkout page
    And the user enters "RANDOM" into the "Zip/Postal Code" field on the checkout page
    And the user clicks the "Continue" button on the checkout page
    Then the user should be on the "Checkout: Overview" page
    And the checkout overview should contain exactly the following products:
      | Sauce Labs Backpack      |
      | Sauce Labs Fleece Jacket |
    And the "Item total" value on the checkout overview should be "Item total: $79.98"
    And the "Tax" value on the checkout overview should be "Tax: $6.40"
    And the "Total" value on the checkout overview should be "Total: $86.38"

  @regression
  @positive
  @severity=minor
  Scenario: The Cancel button on the checkout information page returns the user to the cart
    When the user clicks the "Cancel" button on the checkout page
    Then the user should be on the "Cart" page
