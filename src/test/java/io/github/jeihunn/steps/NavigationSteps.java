package io.github.jeihunn.steps;

import static org.assertj.core.api.Assertions.assertThat;

import io.cucumber.java.en.Then;
import io.github.jeihunn.config.ConfigurationManager;
import io.github.jeihunn.driver.DriverManager;
import io.github.jeihunn.ui.enums.PageRoute;
import io.github.jeihunn.ui.navigation.RouteVerifier;
import org.openqa.selenium.WebDriver;

/** Step definitions for navigation verification. */
public class NavigationSteps {

  @Then("the user should be on the {string} page")
  public void verifyCurrentPage(String expectedPageName) {

    PageRoute route = PageRoute.fromPageName(expectedPageName);
    WebDriver driver = DriverManager.getActiveDriver();

    assertThat(RouteVerifier.isOnRoute(driver, ConfigurationManager.config().baseUrl(), route))
        .as("User is not on expected route: %s", expectedPageName)
        .isTrue();
  }
}
