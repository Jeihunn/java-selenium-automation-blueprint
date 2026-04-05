package io.github.jeihunn.ui.pages;

import io.github.jeihunn.driver.DriverProvider;
import io.github.jeihunn.ui.BasePage;
import java.util.List;
import java.util.Locale;
import org.openqa.selenium.By;

/** Cart page actions and data helpers. */
public class CartPage extends BasePage {

  private static final By CART_ITEMS = By.className("cart_item");
  private static final By PRODUCT_NAMES = By.className("inventory_item_name");
  private static final By CONTINUE_SHOPPING_BUTTON = By.id("continue-shopping");
  private static final By CHECKOUT_BUTTON = By.id("checkout");

  /** Creates the cart page with the shared driver instance. */
  public CartPage(DriverProvider driverProvider) {
    super(driverProvider.get());
  }

  /** Builds the remove-button locator for the provided product name. */
  private By removeButton(String productName) {
    return By.cssSelector("[data-test='remove-" + normalizeProductName(productName) + "']");
  }

  /** Normalizes product names to match data-test locator conventions. */
  private String normalizeProductName(String productName) {
    return productName.trim().toLowerCase(Locale.ROOT).replace(" ", "-");
  }

  /** Removes a product from the cart by product name. */
  public void removeProduct(String productName) {
    click(removeButton(productName));
  }

  /** Clicks the Continue Shopping button. */
  public void clickContinueShopping() {
    click(CONTINUE_SHOPPING_BUTTON);
  }

  /** Clicks the Checkout button. */
  public void clickCheckout() {
    click(CHECKOUT_BUTTON);
  }

  /** Returns all product names currently listed in the cart. */
  public List<String> getCartProductNames() {
    if (isCartEmpty()) {
      return List.of();
    }
    return getTexts(PRODUCT_NAMES);
  }

  /** Returns true when no cart items are present. */
  public boolean isCartEmpty() {
    return !isPresent(CART_ITEMS);
  }
}
