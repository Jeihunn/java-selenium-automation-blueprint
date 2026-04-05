package io.github.jeihunn.ui.enums;

import java.util.Arrays;
import java.util.Locale;

/** Defines canonical UI routes used in navigation assertions. */
public enum PageRoute {
  LOGIN("Login", ""),
  INVENTORY("Inventory", "inventory.html"),
  CART("Cart", "cart.html"),
  CHECKOUT_INFO("Checkout: Your Information", "checkout-step-one.html"),
  CHECKOUT_OVERVIEW("Checkout: Overview", "checkout-step-two.html"),
  CHECKOUT_COMPLETE("Checkout: Complete!", "checkout-complete.html");

  private final String pageName;
  private final String urlPath;

  PageRoute(String pageName, String urlPath) {
    this.pageName = pageName;
    this.urlPath = urlPath;
  }

  /** Returns the human-readable page label used in Gherkin steps. */
  public String getPageName() {
    return pageName;
  }

  /** Returns the route path segment associated with this page. */
  public String getUrlPath() {
    return urlPath;
  }

  /** Returns whether this route points to the base application URL. */
  public boolean isBaseRoute() {
    return urlPath == null || urlPath.isBlank();
  }

  /** Resolves a route from a page name used in step definitions. */
  public static PageRoute fromPageName(String name) {
    String normalizedName = normalizePageName(name);

    return Arrays.stream(values())
        .filter(route -> normalizePageName(route.pageName).equals(normalizedName))
        .findFirst()
        .orElseThrow(
            () ->
                new IllegalArgumentException(
                    "Unknown page name: '"
                        + name
                        + "'. Supported values: "
                        + supportedPageNames()));
  }

  private static String normalizePageName(String name) {
    if (name == null) {
      throw new IllegalArgumentException("Page name cannot be null");
    }

    String normalized = name.trim();
    if (normalized.isEmpty()) {
      throw new IllegalArgumentException("Page name cannot be blank");
    }

    return normalized.toLowerCase(Locale.ROOT);
  }

  private static String supportedPageNames() {
    return Arrays.stream(values()).map(PageRoute::getPageName).toList().toString();
  }
}
