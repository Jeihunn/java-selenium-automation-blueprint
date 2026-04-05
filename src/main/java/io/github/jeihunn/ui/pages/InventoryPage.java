package io.github.jeihunn.ui.pages;

import io.github.jeihunn.driver.DriverProvider;
import io.github.jeihunn.ui.BasePage;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import org.openqa.selenium.By;

/** Encapsulates interactions and data retrieval for the inventory page. */
public class InventoryPage extends BasePage {

  /** Lists supported UI sort options and their select values. */
  public enum SortOption {
    NAME_A_TO_Z("Name (A to Z)", "az"),
    NAME_Z_TO_A("Name (Z to A)", "za"),
    PRICE_LOW_TO_HIGH("Price (low to high)", "lohi"),
    PRICE_HIGH_TO_LOW("Price (high to low)", "hilo");

    private final String uiText;
    private final String value;

    SortOption(String uiText, String value) {
      this.uiText = uiText;
      this.value = value;
    }

    /** Returns the underlying HTML select value for this sort option. */
    public String getValue() {
      return value;
    }

    /** Resolves a sort option by its visible UI text. */
    public static SortOption fromUiText(String text) {
      if (text == null || text.isBlank()) {
        throw new IllegalArgumentException("Sort option cannot be null or blank");
      }

      String normalizedText = text.trim();
      return Arrays.stream(values())
          .filter(option -> option.uiText.equalsIgnoreCase(normalizedText))
          .findFirst()
          .orElseThrow(
              () ->
                  new IllegalArgumentException(
                      "Unknown sort option: '"
                          + text
                          + "'. Supported values: "
                          + supportedValues()));
    }

    private static String supportedValues() {
      return Arrays.stream(values()).map(Enum::name).toList().toString();
    }
  }

  private static final By SORT_DROPDOWN = By.className("product_sort_container");
  private static final By PRODUCT_NAMES = By.className("inventory_item_name");
  private static final By PRODUCT_PRICES = By.className("inventory_item_price");

  /** Creates an inventory page bound to the shared driver instance. */
  public InventoryPage(DriverProvider driverProvider) {
    super(driverProvider.get());
  }

  /** Builds the add-to-cart button locator for the given product name. */
  private By addToCartButton(String productName) {
    return By.cssSelector("[data-test='add-to-cart-" + normalizeProductName(productName) + "']");
  }

  /** Builds the remove button locator for the given product name. */
  private By removeFromCartButton(String productName) {
    return By.cssSelector("[data-test='remove-" + normalizeProductName(productName) + "']");
  }

  /** Normalizes product names to match data-test locator conventions. */
  private String normalizeProductName(String productName) {
    return productName.trim().toLowerCase(Locale.ROOT).replace(" ", "-");
  }

  /** Adds a product to the cart by product name. */
  public void addToCart(String productName) {
    click(addToCartButton(productName));
  }

  /** Removes a product from the cart by product name. */
  public void removeFromCart(String productName) {
    click(removeFromCartButton(productName));
  }

  /** Applies sorting using the visible sort option label. */
  public void sortBy(String uiOption) {
    SortOption option = SortOption.fromUiText(uiOption);
    selectByValue(SORT_DROPDOWN, option.getValue());
    waitForVisible(PRODUCT_NAMES);
  }

  /** Returns all visible product names on the inventory page. */
  public List<String> getProductNames() {
    return getTexts(PRODUCT_NAMES);
  }

  /** Returns all visible product prices as numeric values. */
  public List<Double> getProductPrices() {
    return findAll(PRODUCT_PRICES).stream()
        .map(e -> Double.parseDouble(e.getText().replace("$", "").trim()))
        .toList();
  }
}
