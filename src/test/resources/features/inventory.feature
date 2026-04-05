@inventory
@ui
Feature: Inventory page functionality
  The user should be able to add products to the cart, remove them,
  sort the product list, and log out from the inventory page.

  Background:
    Given the user is on the login page
    When the user enters "standardUser" as the username
    And the user enters "standardUser" as the password
    And the user clicks the Login button
    Then the user should be on the "Inventory" page

  @smoke
  @positive
  @severity=critical
  Scenario: The user can add multiple products to the cart
    When the user adds the following products to the cart
      | Sauce Labs Backpack     |
      | Sauce Labs Bike Light   |
      | Sauce Labs Bolt T-Shirt |
    Then the cart badge should show 3

  @regression
  @positive
  @severity=normal
  Scenario: The user can remove all added products from the cart
    When the user adds the following products to the cart
      | Sauce Labs Backpack     |
      | Sauce Labs Bike Light   |
      | Sauce Labs Bolt T-Shirt |
    And the user removes the added products from the cart
    Then the cart badge should show 0

  @regression
  @severity=minor
  Scenario Outline: Products are sorted correctly for the selected sort option
    When the user sorts the products by "<sortOption>"
    Then the products should be sorted by "<sortOption>"

    Examples:
      | sortOption          |
      | Name (A to Z)       |
      | Name (Z to A)       |
      | Price (low to high) |
      | Price (high to low) |

  @smoke
  @severity=critical
  Scenario: The user logs out successfully
    When the user opens the burger menu
    And the user selects "Logout" from the sidebar menu
    Then the user should be on the "Login" page

  @framework_test
  @severity=normal
  Scenario: Intentionally failing scenario for soft assertion validation
    Then the first product name on the inventory page should be "Sauce Labs Backpackgg"
    And the first product price on the inventory page should be "$299.99"
    When the user adds the following products to the cart
      | Sauce Labs Backpack |
      | Sauce Labs Onesie   |
    Then the cart badge should show 0
    When the user opens the burger menu
    And the user selects "Logout" from the sidebar menu
    Then the user should be on the "Login" page
