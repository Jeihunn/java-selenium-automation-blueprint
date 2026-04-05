package io.github.jeihunn.steps;

import static org.assertj.core.api.Assertions.assertThat;

import io.cucumber.datatable.DataTable;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.github.jeihunn.ui.pages.CartPage;
import java.util.List;

/** Step definitions for cart-specific actions and validations. */
public class CartSteps {

  private final CartPage cartPage;

  /** Creates cart steps with the cart page object. */
  public CartSteps(CartPage cartPage) {
    this.cartPage = cartPage;
  }

  @When("the user clicks the {string} button on the cart page")
  public void clickCartPageButton(String buttonName) {
    switch (buttonName) {
      case "Continue Shopping" -> cartPage.clickContinueShopping();
      case "Checkout" -> cartPage.clickCheckout();
      default -> throw new IllegalArgumentException("Unknown cart page button: " + buttonName);
    }
  }

  @When("the user removes {string} from the cart")
  public void removeProductDirectlyFromCart(String productName) {
    cartPage.removeProduct(productName);
  }

  @Then("the cart should contain exactly the following products")
  public void verifyCartContainsExactProducts(DataTable table) {
    List<String> expectedProducts = table.asList();
    List<String> actualProducts = cartPage.getCartProductNames();

    assertThat(actualProducts)
        .as("Cart contents do not match the expected products")
        .containsExactlyElementsOf(expectedProducts);
  }

  @Then("the cart should not contain {string}")
  public void verifyProductIsNotInCartList(String productName) {
    List<String> actualProducts = cartPage.getCartProductNames();

    assertThat(actualProducts)
        .as("Removed product is still shown in the cart: " + productName)
        .doesNotContain(productName);
  }

  @Then("the cart should be empty")
  public void verifyCartListIsEmpty() {
    assertThat(cartPage.isCartEmpty()).as("Cart is not empty").isTrue();
  }
}
