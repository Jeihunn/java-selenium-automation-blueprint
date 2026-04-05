package io.github.jeihunn.steps;

import static org.assertj.core.api.Assertions.assertThat;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.github.jeihunn.data.TestDataRegistry;
import io.github.jeihunn.ui.pages.CheckoutPage;
import java.util.List;

/** Step definitions for checkout input, actions, and validations. */
public class CheckoutSteps {

  private final CheckoutPage checkoutPage;
  private final TestDataRegistry testDataRegistry;

  /** Creates checkout steps with required collaborators. */
  public CheckoutSteps(CheckoutPage checkoutPage, TestDataRegistry testDataRegistry) {
    this.checkoutPage = checkoutPage;
    this.testDataRegistry = testDataRegistry;
  }

  @When("the user enters {string} into the {string} field on the checkout page")
  public void enterDataToCheckoutField(String value, String fieldName) {
    String finalValue = value;

    if ("RANDOM".equals(value)) {
      finalValue =
          switch (fieldName) {
            case "First Name" -> testDataRegistry.generate().raw().name().firstName();
            case "Last Name" -> testDataRegistry.generate().raw().name().lastName();
            case "Zip/Postal Code" -> testDataRegistry.generate().raw().address().zipCode();
            default ->
                throw new IllegalArgumentException("Unknown RANDOM checkout field: " + fieldName);
          };
    }

    switch (fieldName) {
      case "First Name" -> checkoutPage.enterFirstName(finalValue);
      case "Last Name" -> checkoutPage.enterLastName(finalValue);
      case "Zip/Postal Code" -> checkoutPage.enterZipCode(finalValue);
      default -> throw new IllegalArgumentException("Unknown checkout field: " + fieldName);
    }
  }

  @When("the user clicks the {string} button on the checkout page")
  public void clickCheckoutButton(String buttonName) {
    switch (buttonName) {
      case "Continue" -> checkoutPage.clickContinue();
      case "Cancel" -> checkoutPage.clickCancel();
      case "Finish" -> checkoutPage.clickFinish();
      case "Back Home" -> checkoutPage.clickBackHome();
      default -> throw new IllegalArgumentException("Unknown checkout page button: " + buttonName);
    }
  }

  @Then("the checkout page should display the error message {string}")
  public void verifyCheckoutErrorMessage(String expectedMessage) {
    assertThat(checkoutPage.getErrorMessage())
        .as("Checkout page error message is incorrect")
        .isEqualTo(expectedMessage);
  }

  @Then("the checkout overview should contain exactly the following products:")
  public void verifyOverviewProducts(DataTable table) {
    List<String> expectedProducts = table.asList();
    assertThat(checkoutPage.getOverviewProductNames())
        .as("Checkout overview products do not match the expected products")
        .containsExactlyElementsOf(expectedProducts);
  }

  @Then("the {string} value on the checkout overview should be {string}")
  public void verifyOverviewPriceValues(String labelType, String expectedValue) {
    String actualValue =
        switch (labelType) {
          case "Item total" -> checkoutPage.getItemTotal();
          case "Tax" -> checkoutPage.getTax();
          case "Total" -> checkoutPage.getTotal();
          default ->
              throw new IllegalArgumentException("Unknown checkout overview value: " + labelType);
        };

    assertThat(actualValue)
        .as("Checkout overview value is incorrect for " + labelType)
        .isEqualTo(expectedValue);
  }

  @Then("the checkout complete page should display the message {string}")
  public void verifyCompleteMessage(String expectedMessage) {
    assertThat(checkoutPage.getCompleteMessage())
        .as("Checkout completion message is incorrect")
        .isEqualTo(expectedMessage);
  }
}
