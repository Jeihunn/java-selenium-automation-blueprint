package io.github.jeihunn.steps;

import static org.assertj.core.api.Assertions.assertThat;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.github.jeihunn.ui.components.HeaderComponent;

/** Step definitions for header actions and validations. */
public class HeaderSteps {

  private final HeaderComponent header;

  /** Creates header steps with the shared header component. */
  public HeaderSteps(HeaderComponent header) {
    this.header = header;
  }

  @When("the user opens the cart page")
  public void openCartPage() {
    header.openCart();
  }

  @When("the user opens the burger menu")
  public void openBurgerMenu() {
    header.openMenu();
  }

  @Then("the cart badge should show {int}")
  public void verifyCartBadgeCount(int expected) {
    assertThat(header.getCartCount()).as("Cart badge count is incorrect").isEqualTo(expected);
  }
}
