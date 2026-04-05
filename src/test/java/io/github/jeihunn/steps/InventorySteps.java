package io.github.jeihunn.steps;

import static org.assertj.core.api.Assertions.assertThat;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.github.jeihunn.assertions.ReportingSoftAssertions;
import io.github.jeihunn.context.ScenarioContext;
import io.github.jeihunn.context.ScenarioContextKey;
import io.github.jeihunn.ui.pages.InventoryPage;
import java.util.Comparator;
import java.util.List;

/** Step definitions for inventory actions, sorting, and validations. */
public class InventorySteps {

  private final InventoryPage inventoryPage;
  private final ScenarioContext scenarioContext;
  private final ReportingSoftAssertions softAssertions;

  /** Creates inventory steps with required collaborators. */
  public InventorySteps(
      InventoryPage inventoryPage,
      ScenarioContext scenarioContext,
      ReportingSoftAssertions softAssertions) {
    this.inventoryPage = inventoryPage;
    this.scenarioContext = scenarioContext;
    this.softAssertions = softAssertions;
  }

  @When("the user adds the following products to the cart")
  public void addProductsToCart(DataTable table) {
    List<String> products = table.asList();
    products.forEach(inventoryPage::addToCart);
    scenarioContext.set(ScenarioContextKey.CART_PRODUCTS, products);
  }

  @When("the user removes the added products from the cart")
  public void removeAddedProductsFromCart() {
    List<String> products = scenarioContext.get(ScenarioContextKey.CART_PRODUCTS);
    if (products != null && !products.isEmpty()) {
      products.forEach(inventoryPage::removeFromCart);
    }
  }

  @When("the user sorts the products by {string}")
  public void sortProductsByOption(String option) {
    inventoryPage.sortBy(option);
  }

  @Then("the products should be sorted by {string}")
  public void verifyProductSortOrder(String optionText) {
    InventoryPage.SortOption sort = InventoryPage.SortOption.fromUiText(optionText);

    switch (sort) {
      case NAME_A_TO_Z ->
          assertThat(inventoryPage.getProductNames())
              .as("Product order is incorrect for Name (A to Z)")
              .isSorted();

      case NAME_Z_TO_A ->
          assertThat(inventoryPage.getProductNames())
              .as("Product order is incorrect for Name (Z to A)")
              .isSortedAccordingTo(Comparator.reverseOrder());

      case PRICE_LOW_TO_HIGH ->
          assertThat(inventoryPage.getProductPrices())
              .as("Product order is incorrect for Price (low to high)")
              .isSorted();

      case PRICE_HIGH_TO_LOW ->
          assertThat(inventoryPage.getProductPrices())
              .as("Product order is incorrect for Price (high to low)")
              .isSortedAccordingTo(Comparator.reverseOrder());
    }
  }

  @Then("the first product name on the inventory page should be {string}")
  public void verifyFirstProductName(String expectedName) {
    softAssertions
        .assertThat(inventoryPage.getProductNames())
        .as("First product name on the inventory page is incorrect")
        .isNotEmpty()
        .first()
        .isEqualTo(expectedName);
  }

  @Then("the first product price on the inventory page should be {string}")
  public void verifyFirstProductPrice(String expectedPrice) {
    double expectedPriceDouble = Double.parseDouble(expectedPrice.replace("$", "").trim());

    softAssertions
        .assertThat(inventoryPage.getProductPrices())
        .as("First product price on the inventory page is incorrect")
        .isNotEmpty()
        .first()
        .isEqualTo(expectedPriceDouble);
  }
}
