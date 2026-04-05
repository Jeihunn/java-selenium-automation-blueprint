package io.github.jeihunn.ui.navigation;

import io.github.jeihunn.ui.enums.PageRoute;
import java.util.Objects;
import org.openqa.selenium.WebDriver;

/** Performs route-level assertions against the current browser URL. */
public final class RouteVerifier {

  private RouteVerifier() {}

  /** Returns whether the provided browser URL matches the expected route path. */
  public static boolean isOnRoute(WebDriver driver, String baseUrl, PageRoute route) {
    Objects.requireNonNull(driver, "WebDriver cannot be null");
    Objects.requireNonNull(route, "Route cannot be null");
    Objects.requireNonNull(baseUrl, "Base URL cannot be null");

    String currentUrl = driver.getCurrentUrl();
    return matches(currentUrl, baseUrl, route);
  }

  /** Compares current and expected routes using normalized path values. */
  static boolean matches(String currentUrl, String baseUrl, PageRoute route) {
    Objects.requireNonNull(route, "Route cannot be null");

    String currentPath = RoutePathResolver.extractNormalizedPath(currentUrl);
    String expectedPath = RoutePathResolver.resolveExpectedPath(baseUrl, route.getUrlPath());

    return RoutePathResolver.areSamePath(currentPath, expectedPath);
  }
}
