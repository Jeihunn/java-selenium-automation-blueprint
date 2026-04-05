@cart
@ui
Feature: Cart page functionality
  The user should be able to review the cart, verify added products,
  remove products from the cart, and return to the inventory page
  through different navigation paths.

  Background:
    Given the user is on the login page
    When the user enters "standardUser" as the username
    And the user enters "standardUser" as the password
    And the user clicks the Login button
    Then the user should be on the "Inventory" page

  @smoke
  @positive
  @severity=critical
  Scenario: Added products are displayed correctly in the cart
    When the user adds the following products to the cart
      | Sauce Labs Backpack   |
      | Sauce Labs Bike Light |
    And the user opens the cart page
    Then the cart badge should show 2
    And the cart should contain exactly the following products
      | Sauce Labs Backpack   |
      | Sauce Labs Bike Light |

  @regression
  @positive
  @severity=normal
  Scenario: The user can remove a product directly from the cart
    When the user adds the following products to the cart
      | Sauce Labs Backpack   |
      | Sauce Labs Bike Light |
    And the user opens the cart page
    And the user removes "Sauce Labs Backpack" from the cart
    Then the cart should not contain "Sauce Labs Backpack"
    And the cart badge should show 1

  @regression
  @negative
  @severity=minor
  Scenario: The empty cart state is displayed correctly
    When the user opens the cart page
    Then the cart badge should show 0
    And the cart should be empty

  @smoke
  @severity=normal
  Scenario: The user can continue shopping from the cart page
    When the user opens the cart page
    And the user clicks the "Continue Shopping" button on the cart page
    Then the user should be on the "Inventory" page

  @smoke
  @severity=normal
  Scenario: The user can return to the inventory page from the sidebar menu
    When the user opens the cart page
    And the user opens the burger menu
    And the user selects "All Items" from the sidebar menu
    Then the user should be on the "Inventory" page

  @smoke
  @severity=critical
  Scenario: The user can proceed to the checkout information page from the cart
    When the user adds the following products to the cart
      | Sauce Labs Backpack |
    And the user opens the cart page
    And the user clicks the "Checkout" button on the cart page
    Then the user should be on the "Checkout: Your Information" page
