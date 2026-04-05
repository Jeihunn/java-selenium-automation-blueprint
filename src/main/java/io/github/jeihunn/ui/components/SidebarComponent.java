package io.github.jeihunn.ui.components;

import io.github.jeihunn.driver.DriverProvider;
import io.github.jeihunn.ui.BasePage;
import java.util.Arrays;
import org.openqa.selenium.By;

/** Encapsulates interactions with the application sidebar menu. */
public class SidebarComponent extends BasePage {

  private static final By ALL_ITEMS_LINK = By.id("inventory_sidebar_link");
  private static final By ABOUT_LINK = By.id("about_sidebar_link");
  private static final By LOGOUT_LINK = By.id("logout_sidebar_link");
  private static final By RESET_APP_STATE_LINK = By.id("reset_sidebar_link");

  /** Creates a sidebar component bound to the shared driver instance. */
  public SidebarComponent(DriverProvider driverProvider) {
    super(driverProvider.get());
  }

  /** Lists supported sidebar menu items and their locators. */
  public enum SidebarMenuItem {
    ALL_ITEMS("All Items", ALL_ITEMS_LINK),
    ABOUT("About", ABOUT_LINK),
    LOGOUT("Logout", LOGOUT_LINK),
    RESET_APP_STATE("Reset App State", RESET_APP_STATE_LINK);

    private final String uiText;
    private final By locator;

    SidebarMenuItem(String uiText, By locator) {
      this.uiText = uiText;
      this.locator = locator;
    }

    /** Returns the Selenium locator for this sidebar item. */
    public By getLocator() {
      return locator;
    }

    /** Resolves a sidebar item by its visible UI text. */
    public static SidebarMenuItem fromUiText(String text) {
      if (text == null || text.isBlank()) {
        throw new IllegalArgumentException("Sidebar menu item cannot be null or blank");
      }

      String normalizedText = text.trim();
      return Arrays.stream(values())
          .filter(item -> item.uiText.equalsIgnoreCase(normalizedText))
          .findFirst()
          .orElseThrow(
              () ->
                  new IllegalArgumentException(
                      "Unknown sidebar menu item: '"
                          + text
                          + "'. Supported values: "
                          + supportedValues()));
    }

    private static String supportedValues() {
      return Arrays.stream(values()).map(Enum::name).toList().toString();
    }
  }

  /** Clicks a sidebar menu item by its visible label text. */
  public void clickMenuItem(String itemText) {
    SidebarMenuItem menuItem = SidebarMenuItem.fromUiText(itemText);
    click(menuItem.getLocator());
  }
}
