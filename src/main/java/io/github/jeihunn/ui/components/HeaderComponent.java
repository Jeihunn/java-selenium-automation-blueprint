package io.github.jeihunn.ui.components;

import io.github.jeihunn.driver.DriverProvider;
import io.github.jeihunn.ui.BasePage;
import org.openqa.selenium.By;

/** Header actions used across page objects. */
public class HeaderComponent extends BasePage {

  private static final By CART_BADGE = By.cssSelector(".shopping_cart_badge");
  private static final By CART_LINK = By.className("shopping_cart_link");
  private static final By BURGER_MENU_BUTTON = By.id("react-burger-menu-btn");

  /** Creates the header component with the shared driver instance. */
  public HeaderComponent(DriverProvider driverProvider) {
    super(driverProvider.get());
  }

  /** Returns the cart badge count, or zero when the badge is not visible. */
  public int getCartCount() {
    return isPresent(CART_BADGE) ? Integer.parseInt(getText(CART_BADGE)) : 0;
  }

  /** Opens the shopping cart page. */
  public void openCart() {
    click(CART_LINK);
  }

  /** Opens the sidebar menu from the header. */
  public void openMenu() {
    click(BURGER_MENU_BUTTON);
  }
}
